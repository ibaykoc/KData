package com.github.ibaykoc.kdataprocessor

import com.github.ibaykoc.kdataannotation.KData
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.util.*
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType

data class ValidClass(
    val name: String,
    val packageName: String,
    val fields: MutableList<Field> = mutableListOf(),
    val toValidateField: MutableList<String> = mutableListOf()
) {
    data class Field(val name: String, val typeName: TypeName, val isNullable: Boolean)
}

@AutoService(Processor::class)
class KRawDataAnnotationProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() =
        mutableSetOf(KData::class.java.name, KData.Field::class.java.name)


    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()


    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(KData::class.java).forEach { kRawDataAnnotatedClass ->
            val packageName = processingEnv.elementUtils.getPackageOf(kRawDataAnnotatedClass).toString()
            val validatedClassName = kRawDataAnnotatedClass.getAnnotation(KData::class.java).validatedClassName
            val validClass = ValidClass(validatedClassName, packageName)
            val fieldsToTraverse = Stack<Pair<String, List<VariableElement>>>()
            fieldsToTraverse.push(Pair("", ElementFilter.fieldsIn(kRawDataAnnotatedClass.enclosedElements)))
            do {
                val currentFieldsToTraverse = fieldsToTraverse.pop()
                currentFieldsToTraverse.second.forEach { validateAnnotatedField ->
                    if (validateAnnotatedField.getAnnotation(KData.ParentField::class.java) != null) {
                        val fieldName = validateAnnotatedField.toString()
                        val childFields = (validateAnnotatedField.asType() as DeclaredType).asElement().enclosedElements
                        val root = currentFieldsToTraverse.first + fieldName + "?."
                        fieldsToTraverse.push(Pair(root, ElementFilter.fieldsIn(childFields)))
                    } else if (validateAnnotatedField.getAnnotation(KData.Field::class.java) != null) {
                        val fieldName = validateAnnotatedField.toString()
                        val validateFieldName = validateAnnotatedField.getAnnotation(KData.Field::class.java).validatedFieldName
                        val allowNull = validateAnnotatedField.getAnnotation(KData.Field::class.java).allowNull
                        val fieldType = validateAnnotatedField.asType().asTypeName().javaToKotlinType()
                        val root = currentFieldsToTraverse.first
                        validClass.fields.add(ValidClass.Field(validateFieldName, fieldType, allowNull))
                        var validateFieldStatement = "$root$fieldName?"
                        if(allowNull) validateFieldStatement = validateFieldStatement.replace("?","")
                        validClass.toValidateField.add(validateFieldStatement)
                    }
                }
            } while (!fieldsToTraverse.empty())
            val fileBuilder = FileSpec.builder(validClass.packageName, validClass.name)
            val validDataClass = TypeSpec.classBuilder(validClass.name).apply {
                addModifiers(KModifier.DATA)
                val constructor = FunSpec.constructorBuilder().apply {
                    validClass.fields.forEach {
                        addParameter(it.name, it.typeName.copy(nullable = it.isNullable))
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
                .receiver(kRawDataAnnotatedClass.asType().asTypeName())
                .returns(ClassName(packageName, validClass.name).copy(nullable = true))
            validClass.toValidateField.forEachIndexed { i, s ->
                var condition = ""
                if (i == 0) condition += "return "
                condition += "$s.let"
                validateExtFunBuilder.beginControlFlow(condition)
            }
            validateExtFunBuilder.addStatement(
                "return ${validClass.name}(${validClass.toValidateField.joinToString(
                    separator = ",\n",
                    transform = { it.replace("?", "") })})"
            )
            validClass.toValidateField.forEach {
                validateExtFunBuilder.endControlFlow()
            }

            fileBuilder.addAnnotation(ClassName("", "Suppress(\"UNNECESSARY_SAFE_CALL\")"))
            fileBuilder.addType(validDataClass)
            fileBuilder.addFunction(validateExtFunBuilder.build())

            val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
            fileBuilder.build().writeTo(File(kaptGeneratedDir, "${validClass.name}.kt"))
        }
        return true
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