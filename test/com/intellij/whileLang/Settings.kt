package com.intellij.whileLang

import com.intellij.psi.PsiDeclarationStatement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiVariable
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import com.intellij.whileLang.printer.WhilePrinter
import org.jetbrains.prettyPrinter.core.util.string.lineEndTrim
import java.io.File
import kotlin.test.assertEquals

/**
 * User: anlun
 */
val PATH_TO_COMPONENTS: String = "testData${File.separator}"
val MAX_WIDTH: Int = 40

abstract class ComponentTest(): LightCodeInsightFixtureTestCase() {
    abstract fun getOurPathToComponents(): String

    open fun getOurMaxWidth(): Int = MAX_WIDTH

    fun getTest(
            templateFileName: String
            , toReprintFileName: String
    ): String {
        val templateFileName = getOurPathToComponents() + templateFileName
        val templateFileContent = File(templateFileName).readText()

        val project = getProject()
        if (project == null) { throw NullPointerException() }

        val tmpltPsiFile =
                PsiFileFactory.getInstance(project)!!.createFileFromText(templateFileName, templateFileContent)
                        as WhileFile

        val toReprintFileName = getOurPathToComponents() + toReprintFileName
        val toReprintFileContent = File(toReprintFileName).readText()
        val toReprintPsiFile =
                PsiFileFactory.getInstance(project)!!.createFileFromText(toReprintFileName, toReprintFileContent)
                        as WhileFile

        val printer = WhilePrinter.create(tmpltPsiFile, project, getOurMaxWidth())
        val variants = printer.getVariants(toReprintPsiFile)
        val format = variants.head()
        val text = if (format != null) format.toText(0, "") else ""

        return text.lineEndTrim()
    }

    fun test(testTemplateFileName: String, testExampleFileName: String, expectedResultFileName: String) {
        val text = getTest(testTemplateFileName, testExampleFileName)
        val expectedResultFileName = getOurPathToComponents() + expectedResultFileName
        val expectedResult = File(expectedResultFileName).readText()
        assertEquals(expectedResult.trim().lineEndTrim(), text.lineEndTrim(), "Incorrect result!")
    }


    fun getVariableFromText(variableText: String): PsiVariable {
        val factory = getElementFactory()
        val variableDeclarationStatement =
                factory!!.createStatementFromText(variableText, null) as? PsiDeclarationStatement
        val variable =
                variableDeclarationStatement!!.getDeclaredElements()[0] as PsiVariable
        return variable
    }
}