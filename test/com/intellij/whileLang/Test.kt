package com.intellij.whileLang

import com.intellij.whileLang.printer.WhilePrinter
import org.jetbrains.prettyPrinter.core.util.string.lineEndTrim
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * User: anlun
 */
public class ExpressionTests : ComponentTest() {
    override fun getOurPathToComponents(): String = PATH_TO_COMPONENTS + "while${File.separator}"

    fun getTestStatement(
            templateFileName: String
            , toReprintFileName: String
    ): String {
        val templateFileName = getOurPathToComponents() + templateFileName
        val templateFileContent = File(templateFileName).readText()

        val project = getProject()
        if (project == null) {
            throw NullPointerException()
        }

        val whileElementFactory = WhileElementFactory(project)
        val toReprintFileName = getOurPathToComponents() + toReprintFileName
        val toReprintFileContent = File(toReprintFileName).readText().trim()

        val tmpltFile = whileElementFactory.createFileFromText(templateFileContent)
        val printer = WhilePrinter.create(tmpltFile, project, MAX_WIDTH)

        val stmtToReprint =
                whileElementFactory.createFileFromText(toReprintFileContent)
        val variants = printer.getVariants(stmtToReprint)

        assertEquals(1, variants.size(), "More then one variant!")
        val format = variants.head()
        val text = if (format != null) format.toText(0, "") else ""

        return text.lineEndTrim()
    }

    private fun testBody(templateFileName: String, toReprintFileName: String, expectedFileName: String) {
        val text = getTestStatement(templateFileName, toReprintFileName)
        val expectedResultFileName = getOurPathToComponents() + expectedFileName
        val expectedResult = File(expectedResultFileName).readText()
        assertEquals(expectedResult.trim().lineEndTrim(), text.lineEndTrim(), "Incorrect result!")
    }

    Test fun testLet() { testBody("LetT.l", "LetEx.l", "LetExpected.l") }
    Test fun testProc() { testBody("ProcT.l", "ProcEx.l", "ProcExpected.l") }
    Test fun testProc1() { testBody("ProcT1.l", "ProcEx1.l", "ProcExpected1.l") }
}
