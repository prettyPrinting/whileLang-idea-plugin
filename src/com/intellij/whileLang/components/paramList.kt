package com.intellij.whileLang.components

import com.intellij.psi.PsiElement
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiParamList
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.templateBase.template.Template
import org.jetbrains.prettyPrinter.core.util.psiElement.deleteSpaces
import java.util.ArrayList

public class ParamListComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiParamList, SmartInsertPlace, ListTemplate<PsiParamList, SmartInsertPlace>>(printer)
        , ListComponent<PsiParamList>
{
    override public fun getNormalizedElement(p: PsiParamList): PsiParamList? {
        val normalizedText = p.deleteSpaces()
        val element = getListElement(normalizedText)
        return element
    }

    override public fun getListElement(text: String): PsiParamList? {
        try {
            val elementFactory = WhileElementFactory(printer.getProject())
            val newP = elementFactory.createParamListFromText(text)
            return newP as? PsiParamList
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun isTemplateSuitable(
            p: PsiParamList, tmplt: ListTemplate<PsiParamList, org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace>
    ): Boolean = true

    override protected fun getList(p: PsiParamList) = p.getRefExprList().toList()
}