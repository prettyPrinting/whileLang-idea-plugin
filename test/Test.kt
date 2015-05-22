package org.jetbrains.likePrinter.haskell

import com.intellij.lang.java.JavaLanguage
import com.intellij.lineEndTrim
import com.intellij.psi.PsiFileFactory
import com.intellij.whileLang.Printer
import com.intellij.whileLang.WhileElementFactory
import org.jetbrains.likePrinter.whileLang.ComponentTest
import org.jetbrains.likePrinter.whileLang.MAX_WIDTH
import org.jetbrains.likePrinter.whileLang.PATH_TO_COMPONENTS
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * User: anlun
 */
public class ExpressionTests : ComponentTest() {
    override fun getOurPathToComponents(): String = PATH_TO_COMPONENTS + "while/"

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
        val printer = Printer.create(tmpltFile, project, MAX_WIDTH)

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
}
