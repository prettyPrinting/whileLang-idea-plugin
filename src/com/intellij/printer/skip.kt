package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.psi.impl.PsiSkipStmt


public class SkipStmtComponent(
        printer: Printer
): PsiElementComponent<PsiSkipStmt, SmartInsertPlace, PsiTemplateGen<PsiSkipStmt, SmartInsertPlace>>(printer)
   
{

    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: WhileElementFactory
    ): PsiSkipStmt? {
        try {
            val newP = elementFactory.createStatementFromText(text, null)
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