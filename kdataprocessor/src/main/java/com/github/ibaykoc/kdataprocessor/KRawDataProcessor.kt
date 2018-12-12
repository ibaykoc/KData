package com.github.ibaykoc.kdataprocessor

import com.github.ibaykoc.kdataannotation.KData
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

data class ValidClass(
    val name: String,
    val packageName: String,
    val fields: MutableList<Field> = mutableListOf()
) {
    data class Field(val name: String, val typeName: TypeName, val rawAbsolutePath: String, val isNullable: Boolean)
}

@Suppress("unused")
@AutoService(Processor::class)
class KRawDataAnnotationProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(KData::class.java.name, KData.Field::class.java.name)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()


    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(KData::class.java).forEach { kDataAnnotatedClass ->
            createValidatedClass(kDataAnnotatedClass)
        }
        return true
    }

    private fun createValidatedClass(kDataAnnotatedClass: Element) {
        val className = kDataAnnotatedClass.simpleName
        val annotationValidatedClassName = kDataAnnotatedClass.getAnnotation(KData::class.java).validatedClassName
        val validatedClassName =
            if (annotationValidatedClassName.isBlank()) "${className}Validated" else annotationValidatedClassName
        val packageName = processingEnv.elementUtils.getPackageOf(kDataAnnotatedClass).toString()
        val validClass = ValidClass(validatedClassName, packageName)
        val fieldsToTraverse = Stack<Pair<String, List<VariableElement>>>()
        fieldsToTraverse.push(Pair("", ElementFilter.fieldsIn(kDataAnnotatedClass.enclosedElements)))
        do {
            val (path, currentFieldsToTraverse) = fieldsToTraverse.pop()
            currentFieldsToTraverse.forEach { validateAnnotatedField ->
                if (validateAnnotatedField.getAnnotation(KData.ParentField::class.java) != null) {
                    val fieldName = validateAnnotatedField.toString()
                    val childFields = (validateAnnotatedField.asType() as DeclaredType).asElement().enclosedElements
                    val currentPath = "$path$fieldName?."
                    fieldsToTraverse.push(Pair(currentPath, ElementFilter.fieldsIn(childFields)))
                } else if (validateAnnotatedField.getAnnotation(KData.Field::class.java) != null) {
                    val annotationFieldName =
                        validateAnnotatedField.getAnnotation(KData.Field::class.java).validatedFieldName
                    val fieldName = validateAnnotatedField.toString()
                    val validateFieldName =
                        if (annotationFieldName.isBlank()) "${fieldName}Validated" else annotationFieldName
                    val allowNull = validateAnnotatedField.getAnnotation(KData.Field::class.java).allowNull
                    val fieldType = validateAnnotatedField.asType().asTypeName().javaToKotlinType()
                    val currentPath = "$path$fieldName"
                    validClass.fields.add(
                        ValidClass.Field(
                            validateFieldName,
                            fieldType,
                            currentPath,
                            allowNull
                        )
                    )
                }
            }
        } while (!fieldsToTraverse.empty())
        val fileBuilder = FileSpec.builder(validClass.packageName, validClass.name)
        val validDataClass = TypeSpec.classBuilder(validClass.name).apply {
            addModifiers(KModifier.DATA)
            val constructor = FunSpec.constructorBuilder().apply {
                validClass.fields.forEach {
                    addParameter(
                        if (it.isNullable)
                            ParameterSpec.builder(
                                it.name,
                                it.typeName.copy(nullable = it.isNullable)
                            )
                                .defaultValue("null")
                                .build()
                        else
                            ParameterSpec.builder(
                                it.name,
                                it.typeName.copy(nullable = it.isNullable)
                            )
                                .build()
                    )
                }
                build()
            }.build()
            primaryConstructor(constructor)
            addProperties(
                validClass.fields.map {
                    PropertySpec.builder(it.name, it.typeName.copy(nullable = it.isNullable))
                        .initializer(it.name)
                        .build()
                }
            )
        }.build()
        val validateExtFunBuilder = FunSpec.builder("validate")
            .receiver(kDataAnnotatedClass.asType().asTypeName())
            .returns(ClassName(packageName, validClass.name).copy(nullable = true))
            .addCode("return")
        val condition = " if (" +
                validClass.fields.filter { !it.isNullable }
                    .joinToString(
                        separator = " && ",
                        transform = { "${it.rawAbsolutePath} != null" }
                    ) +
                ")"
        validateExtFunBuilder.beginControlFlow(condition)
        validateExtFunBuilder.addStatement(
            "${validClass.name}(${validClass.fields.joinToString(
                separator = ",\n",
                transform = { it.rawAbsolutePath }
            )})"
        )
        validateExtFunBuilder.nextControlFlow("else")
        validateExtFunBuilder.addStatement("null")
        validateExtFunBuilder.endControlFlow()

        fileBuilder.addAnnotation(ClassName("", "Suppress(\"UNNECESSARY_SAFE_CALL\")"))
        fileBuilder.addType(validDataClass)
        fileBuilder.addFunction(validateExtFunBuilder.build())

        val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
        fileBuilder.build().writeTo(File(kaptGeneratedDir, "${validClass.name}.kt"))
    }

    private fun TypeName.javaToKotlinType(): TypeName = if (this is ParameterizedTypeName) {
        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
            *typeArguments.map { it.javaToKotlinType() }.toTypedArray()
        )
    } else {
        val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
        if (className == null) this
        else ClassName.bestGuess(className)
    }
}