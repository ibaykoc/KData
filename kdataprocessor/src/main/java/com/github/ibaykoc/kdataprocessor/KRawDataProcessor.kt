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
import javax.tools.Diagnostic
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

internal data class KDataBlueprint(
    val name: String,
    val packageName: String,
    val rawTypeName: TypeName,
    val fields: MutableList<Field> = mutableListOf(),
    val children: MutableList<KDataBlueprint> = mutableListOf()
) {
    data class Field(
        val name: String,
        val typeName: TypeName,
        val rawAbsolutePath: List<String>,
        val isNullable: Boolean = false,
        val isList: Boolean = false,
        val isParent: Boolean = false
    )
}

@Suppress("unused")
@AutoService(Processor::class)
class KRawDataAnnotationProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(KData::class.java.name, KData.Field::class.java.name)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(KData::class.java).forEach {
            val rootValidClass = createKDataBlueprint(it)
            generateFile(rootValidClass)
        }
        return true
    }

    private fun createKDataBlueprint(
        element: Element,
        pathFromRoot: List<String> = emptyList(),
        packageName: String? = null
    ): KDataBlueprint {
        val kDataBlueprintName = namingKData(element)
        val kDataPackageName = packageName ?: processingEnv.elementUtils.getPackageOf(element).toString()
        val kDataBlueprint = KDataBlueprint(kDataBlueprintName, kDataPackageName, element.asType().asTypeName())
        ElementFilter.fieldsIn(element.enclosedElements).forEach { elementFields ->
            if (elementFields.getAnnotation(KData.ParentField::class.java) != null) {
                val fieldName = elementFields.simpleName.toString()
                val currentPath = pathFromRoot.toMutableList().also { it.add(fieldName) }
                val validFieldName = namingKData(elementFields)
                val isList = isList(elementFields)
                if (isList) {
                    val parameterizedTypeName =
                        (elementFields.asType().asTypeName() as ParameterizedTypeName).typeArguments.first()
                    val fieldPackageName = "$kDataPackageName.$kDataBlueprintName"
                    kDataBlueprint.fields.add(
                        KDataBlueprint.Field(
                            name = validFieldName,
                            typeName = ClassName(
                                fieldPackageName,
                                (parameterizedTypeName as ClassName).simpleName + "Validated"
                            ),
                            rawAbsolutePath = currentPath,
                            isList = isList,
                            isParent = true
                        )
                    )
                    kDataBlueprint.children.add(
                        createKDataBlueprint(
                            processingEnv.elementUtils.getTypeElement(
                                parameterizedTypeName.toString()
                            ),
                            currentPath,
                            fieldPackageName
                        )
                    )
                } else {
                    val fieldPackageName = "$kDataPackageName.$kDataBlueprintName"
                    kDataBlueprint.fields.add(
                        KDataBlueprint.Field(
                            name = validFieldName,
                            typeName = ClassName(
                                fieldPackageName,
                                (elementFields.asType().asTypeName() as ClassName).simpleName + "Validated"
                            ),
                            rawAbsolutePath = currentPath,
                            isList = isList,
                            isParent = true
                        )
                    )
                    kDataBlueprint.children.add(
                        createKDataBlueprint(
                            (elementFields.asType() as DeclaredType).asElement(),
                            currentPath,
                            fieldPackageName
                        )
                    )
                }
            } else if (elementFields.getAnnotation(KData.Field::class.java) != null) {
                val fieldName = elementFields.simpleName.toString()
                val currentPath = pathFromRoot.toMutableList().also { it.add(fieldName) }
                val validFieldName = namingKData(elementFields)
                val fieldKotlinTypeName: TypeName
                val isList = isList(elementFields)
                val isNullable = elementFields.getAnnotation(KData.Field::class.java).allowNull
                if (isList) {
                    val parameterizedTypeName =
                        (elementFields.asType().asTypeName() as ParameterizedTypeName).typeArguments.first()
                    fieldKotlinTypeName = parameterizedTypeName.javaToKotlinType()

                    kDataBlueprint.fields.add(
                        KDataBlueprint.Field(
                            name = validFieldName,
                            typeName = fieldKotlinTypeName,
                            rawAbsolutePath = currentPath,
                            isList = isList
                        )
                    )
                } else {
                    fieldKotlinTypeName = elementFields.asType().asTypeName().javaToKotlinType()
                    kDataBlueprint.fields.add(
                        KDataBlueprint.Field(
                            name = validFieldName,
                            typeName = fieldKotlinTypeName,
                            rawAbsolutePath = currentPath,
                            isList = isList,
                            isNullable = isNullable
                        )
                    )
                }
            }
        }
        return kDataBlueprint
    }

    private fun namingKData(element: Element): String {
        val userDefinedName = when {
            element.getAnnotation(KData::class.java) != null -> {
                element.getAnnotation(KData::class.java).validatedClassName
            }
            element.getAnnotation(KData.ParentField::class.java) != null -> {
                element.getAnnotation(KData.ParentField::class.java).validatedFieldName
            }
            element.getAnnotation(KData.Field::class.java) != null -> {
                element.getAnnotation(KData.Field::class.java).validatedFieldName
            }
            else -> ""
        }
        return if (userDefinedName.isBlank()) "${element.simpleName}Validated" else userDefinedName
    }

    private fun generateFile(rootKDataBlueprint: KDataBlueprint) {
        val fileBuilder = FileSpec.builder(rootKDataBlueprint.packageName, rootKDataBlueprint.name)
        val kDataClassSpec = createKDataClassSpec(rootKDataBlueprint)
        val validateFunSpecs = createValidateFunSpec(rootKDataBlueprint)
        fileBuilder.addAnnotation(ClassName("", "Suppress(\"UNNECESSARY_SAFE_CALL\")"))
        fileBuilder.addType(kDataClassSpec)
        validateFunSpecs.forEach { fileBuilder.addFunction(it) }
        val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
        fileBuilder.build().writeTo(File(kaptGeneratedDir, "${rootKDataBlueprint.name}.kt"))
    }

    private fun createKDataClassSpec(kDataBlueprint: KDataBlueprint): TypeSpec {
        return TypeSpec.classBuilder(kDataBlueprint.name.capitalize()).apply {
            addModifiers(KModifier.DATA)
            val constructor = FunSpec.constructorBuilder().apply {
                kDataBlueprint.fields.forEach {
                    addParameter(
                        ParameterSpec.builder(
                            it.name,
                            (if (it.isList) ClassName(
                                "kotlin.collections",
                                "List"
                            ).parameterizedBy(it.typeName) else it.typeName).copy(nullable = it.isNullable)
                        ).apply {
                            if (it.isNullable) this.defaultValue("null")
                        }.build()
                    )
                }
                build()
            }.build()
            primaryConstructor(constructor)
            addProperties(
                kDataBlueprint.fields.map {
                    PropertySpec.builder(
                        it.name,
                        (if (it.isList) ClassName(
                            "kotlin.collections",
                            "List"
                        ).parameterizedBy(it.typeName) else it.typeName).copy(nullable = it.isNullable)
                    )
                        .initializer(it.name)
                        .build()
                }
            )
            kDataBlueprint.children.forEach {
                addType(createKDataClassSpec(it))
            }
        }.build()
    }

    private fun createValidateFunSpec(kDataBlueprint: KDataBlueprint): List<FunSpec> {
        val funSpecs = mutableListOf<FunSpec>()
        val kDataBluePrintStack = Stack<KDataBlueprint>()
        kDataBluePrintStack.push(kDataBlueprint)
        while (kDataBluePrintStack.isNotEmpty()) {
            val currentKDataBlueprint = kDataBluePrintStack.pop()
            val validateExtFunBuilder = FunSpec.builder("validate")
                .receiver(currentKDataBlueprint.rawTypeName)
                .returns(ClassName(currentKDataBlueprint.packageName, currentKDataBlueprint.name).copy(nullable = true))
                .addCode("return")
            val condition = " if (" +
                    currentKDataBlueprint.fields.filter { !it.isNullable }
                        .joinToString(
                            separator = " && ",
                            transform = { "${it.rawAbsolutePath.last() + if (it.isParent && !it.isList) "?.validate()" else ""}  != null" }
                        ) +
                    ")"
            validateExtFunBuilder.beginControlFlow(condition)
            validateExtFunBuilder.addStatement(
                "${currentKDataBlueprint.name}(${currentKDataBlueprint.fields.joinToString(
                    separator = ",\n",
                    transform = { it.rawAbsolutePath.last() + if (it.isList) ".mapNotNull { it${if (it.isParent) "?.validate()" else ""} }" else if (it.isParent) ".validate()!!" else "" }
                )})"
            )
            validateExtFunBuilder.nextControlFlow("else")
            validateExtFunBuilder.addStatement("null")
            validateExtFunBuilder.endControlFlow()
            funSpecs.add(validateExtFunBuilder.build())
            currentKDataBlueprint.children.forEach { kDataBluePrintStack.push(it) }
        }
        return funSpecs
    }

    private fun isList(variableElement: VariableElement): Boolean {
        return (variableElement.asType().asTypeName() as? ParameterizedTypeName)?.let {
            Class.forName(it.rawType.toString()).kotlin.isSubclassOf(Collection::class)
        } ?: false
    }


    private fun warning(msg: String) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.WARNING,
            msg
        )
    }

    private fun TypeName.javaToKotlinType(): TypeName = if (this is ParameterizedTypeName) {
        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
            *typeArguments.map { it.javaToKotlinType() }.toTypedArray()
        )
    } else {
        val className =
            JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
        if (className == null) this
        else ClassName.bestGuess(className)
    }
}