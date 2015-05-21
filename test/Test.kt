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
        if (project == null) { throw NullPointerException() }

        val haskellElementFactory = WhileElementFactory(project)

        //val tmpltStatement = HaskellElementFactory!!.createExpressionFromText(project, templateFileContent)

        val toReprintFileName = getOurPathToComponents() + toReprintFileName
        val toReprintFileContent = File(toReprintFileName).readText().trim()
        val stmtToReprint =
                haskellElementFactory.createFileFromText(toReprintFileContent)

        //        val haskFile = PsiFileFactory.getInstance(project).createFileFromText("tmp.hs", HaskellLanguage.INSTANCE, templateFileContent) as HaskellFile
        //        val module = haskFile.getChildren().firstOrNull()!! as Module
        //        val signDecl = (module.getSignatureDeclarationsList() as Iterable<SignatureDeclaration>).firstOrNull() as? SignatureDeclaration
        //        val type = signDecl?.getType()
        //        val signExpr = signDecl?.getQNameExpression()


        val printer = Printer.create(haskellElementFactory.createFileFromText(templateFileContent), project, MAX_WIDTH)

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
    /*
    Test fun testLet() { testBody("LetT.hs", "LetEx.hs", "LetExpected.hs") }
    Test fun testModule() { testBody("ModuleT.hs", "ModuleEx.hs", "ModuleExpected.hs") }
    Test fun testWhere() { testBody("WhereT.hs", "WhereEx.hs", "WhereExpected.hs") }
    Test fun testSignatureDecl() { testBody("SignatureDeclT.hs", "SignatureDeclEx.hs", "SignatureDeclExpected.hs") }
    Test fun testTypeSyn() { testBody("TypeSynT.hs", "TypeSynEx.hs", "TypeSynExpected.hs") }
    Test fun testData() { testBody("DataT.hs", "DataEx.hs", "DataExpected.hs") }
    Test fun testGuard() { testBody("GuardT.hs", "GuardEx.hs", "GuardExpected.hs") }
    */
}