package com.codingventures.dbannotations

import com.google.auto.service.AutoService
import javax.lang.model.element.TypeElement
import com.codingventures.annotations.Column
import com.codingventures.annotations.Table
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(DbAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class DbAnnotationProcessor : AbstractProcessor() {
    data class FieldDescriptor(
        val fieldName: String,
        val fieldJavaType: Class<Element>,
        val columnName: String,
        val isExcluded: Boolean
    )

    data class StatementAccumulator(
        val index: Int = 1,
        val firstSection: String,       // INSERT INTO table (
        val secondSection: String      // VALUES (
//        val thirdSection: String        // ON CONFLICT () DO NOTHING
    )

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(Column::class.java.name, Table::class.java.name)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val annotatedEls = roundEnv.getElementsAnnotatedWith(Table::class.java)
        if (annotatedEls.isEmpty()) {
            return false
        }

        annotatedEls.forEach { element ->
            if (element.kind != ElementKind.CLASS) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "@Table annotation only applies to classes -  ${element.simpleName}"
                )
                return false
            }

            val pkg = processingEnv.elementUtils.getPackageOf(element).toString()
            // Elements declared at root namespace, return this package name which breaks code generation
            val fixedPkg = if (pkg == "unnamed package") "" else pkg

            val fileSpec = generateFileSpec(element, fixedPkg)
            if (fileSpec == null) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "${element.simpleName} does not contain any fields annotated with @Column"
                )
            } else {
                val genDirectory = processingEnv.options["kapt.kotlin.generated"]!!
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "The file for ${element.simpleName} is written to $genDirectory \n"
                )

                fileSpec.writeTo(File(genDirectory))
            }
        }
        return true
    }

    private fun generateFileSpec(element: Element, pkg: String): FileSpec? {
        val fieldDescriptors = getFieldDescriptors(element)
        if (fieldDescriptors.isEmpty()) {
            return null
        }

        val className = element.simpleName.toString()

        val tableAnnotation = element.getAnnotation(Table::class.java)

        val objectName = "${className}DbHelper"

        return FileSpec.builder(pkg, "${className}Generated")
            .addFunction(buildFieldValuesAsList(element, fieldDescriptors))
            .addType(generateHelperObject(objectName, tableAnnotation.name, tableAnnotation.primaryKey, fieldDescriptors))
            .build()
    }


    private fun generateHelperObject(
        objectName: String,
        tableName: String,
        primaryKey: String,
        fieldDescriptors: List<FieldDescriptor>
    ): TypeSpec {
        return TypeSpec.objectBuilder(objectName)
            .addFunction(buildInsertStatement(tableName, primaryKey, fieldDescriptors))
            .build()
    }

    private fun buildInsertStatement(
        tableName: String,
        primaryKey: String,
        fieldDescriptors: List<FieldDescriptor>
    ): FunSpec {
        if (fieldDescriptors.isEmpty()) return emptyFun()

        val seed = StatementAccumulator(
            firstSection = "INSERT INTO $tableName (",
            secondSection = "VALUES ("
//            thirdSection = "ON CONFLICT ($primaryKey) DO NOTHING"
        )

        val statementSections = fieldDescriptors.fold(seed) { acc, fieldDescriptor ->
            if (fieldDescriptor.isExcluded) {
                acc
            } else {
                StatementAccumulator(
                    index = acc.index + 1,
                    firstSection = "${acc.firstSection}${fieldDescriptor.columnName},",
                    secondSection = "${acc.secondSection}\$${acc.index},"
                )
            }
        }
        val firstSection = statementSections.firstSection.subSequence(0, statementSections.firstSection.length - 1)
        val secondSection = statementSections.secondSection.subSequence(0, statementSections.secondSection.length - 1)
        val thirdSection = "ON CONFLICT ($primaryKey) DO NOTHING"
        val insertStatement = "$firstSection) $secondSection) $thirdSection"

        return FunSpec.builder("generateInsertStatement")
            .returns(String::class)
            .addStatement("return %P", insertStatement)
            .build()
    }

    private fun emptyFun(): FunSpec = FunSpec.builder("generateInsertStatement")
        .returns(String::class)
        .addStatement("return \"\"")
        .build()

    @SuppressWarnings("deprecated")
    private fun buildFieldValuesAsList(element: Element, fieldDescriptors: List<FieldDescriptor>): FunSpec {
        val fieldNames = fieldDescriptors
            .filter { !it.isExcluded }
            .map { it.fieldName }
        return FunSpec.builder("valuesAsList")
            .receiver(element.asType().asTypeName().copy())
            .addStatement("return listOf(${fieldNames.joinToString(", ")})")
            .addModifiers(KModifier.PUBLIC)
            .build()
    }

    private fun getFieldDescriptors(element: Element) =
        element.enclosedElements
            .filter {
                it.getAnnotation(Column::class.java) != null
            }
            .map {
                val annotationEl = it.getAnnotation(Column::class.java)

                // The name are appended by $annotations, we need to strip them out
                val field = if (it.simpleName.endsWith("\$annotations")) {
                    // Support by lazy
                    it.simpleName.subSequence(0, it.simpleName.indexOf("$"))
                } else it.simpleName

                FieldDescriptor(
                    fieldName = field.toString(),
                    fieldJavaType = it.javaClass,
                    columnName = annotationEl.name,
                    isExcluded = annotationEl.exclude
                )
            }
}

