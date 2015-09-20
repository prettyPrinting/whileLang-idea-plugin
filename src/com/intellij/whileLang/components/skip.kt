package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiSkipStmt
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.util.psiElement.getCorrectTextOffset
import java.util.HashMap
import java.util.HashSet


public class SkipStmtComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiSkipStmt, SmartInsertPlace, PsiTemplateGen<PsiSkipStmt, SmartInsertPlace>>(printer)
   
{

    
    
    override protected fun getNewElement(
            text: String
    ): PsiSkipStmt? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
            val newP = elementFactory.createSkipStmtFromText(text)
            return newP as? PsiSkipStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiSkipStmt
            , tmplt   : PsiTemplateGen<PsiSkipStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiSkipStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiSkipStmt): Set<String> {
        val set = HashSet<String>()
    
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiSkipStmt
            , tmplt: PsiTemplateGen<PsiSkipStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiSkipStmt): PsiTemplateGen<PsiSkipStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}