package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.psi.impl.PsiNotBexpr


public class NotBexprComponent(
        printer: Printer
): PsiElementComponent<PsiNotBexpr, SmartInsertPlace, PsiTemplateGen<PsiNotBexpr, SmartInsertPlace>>(printer)
   
{

    final val BEXPR_TAG: String
        get() = "bexpr"
    
    private fun addBexprToInsertPlaceMap(
            p: PsiNotBexpr
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val bexpr = p.getBexpr()
        val bexprTextRange = bexpr?.getTextRange()
        if (bexprTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(bexprTextRange)
        
        insertPlaceMap.put(
               BEXPR_TAG
               , SmartInsertPlace(bexprTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareBexprVariants(
            p: PsiNotBexpr
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val bexprVariants = getBexprVariants(p, context)
        if (bexprVariants.isEmpty()) { return }
        variants.put(BEXPR_TAG, bexprVariants)
    }
    
    private fun getBexprVariants(
            p: PsiNotBexpr
            , context: VariantConstructionContext
    ): FormatSet {
        val bexpr = p.getBexpr()
        if (bexpr == null) { return printer.getEmptySet() }
        return printer.getVariants(bexpr, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: WhileElementFactory
    ): PsiNotBexpr? {
        try {
            val newP = elementFactory.createNotBexprFromText(text)
            return newP as? PsiNotBexpr
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiNotBexpr
            , tmplt   : PsiTemplateGen<PsiNotBexpr, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiNotBexpr
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareBexprVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiNotBexpr): Set<String> {
        val set = HashSet<String>()
    
        if (p.getBexpr() != null) { set.add(BEXPR_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiNotBexpr
            , tmplt: PsiTemplateGen<PsiNotBexpr, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiNotBexpr): PsiTemplateGen<PsiNotBexpr, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addBexprToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}