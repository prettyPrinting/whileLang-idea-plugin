package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.psi.impl.PsiParenBexpr


public class ParenBexprComponent(
        printer: Printer
): PsiElementComponent<PsiParenBexpr, SmartInsertPlace, PsiTemplateGen<PsiParenBexpr, SmartInsertPlace>>(printer)
   
{

    final val BEXPR_TAG: String
        get() = "bexpr"
    
    private fun addBexprToInsertPlaceMap(
            p: PsiParenBexpr
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
            p: PsiParenBexpr
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val bexprVariants = getBexprVariants(p, context)
        if (bexprVariants.isEmpty()) { return }
        variants.put(BEXPR_TAG, bexprVariants)
    }
    
    private fun getBexprVariants(
            p: PsiParenBexpr
            , context: VariantConstructionContext
    ): FormatSet {
        val bexpr = p.getBexpr()
        if (bexpr == null) { return printer.getEmptySet() }
        return printer.getVariants(bexpr, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: WhileElementFactory
    ): PsiParenBexpr? {
        try {
            val newP = elementFactory.createBexpressionFromText(text, null)
            return newP as? PsiParenBexpr
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiParenBexpr
            , tmplt   : PsiTemplateGen<PsiParenBexpr, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiParenBexpr
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareBexprVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiParenBexpr): Set<String> {
        val set = HashSet<String>()
    
        if (p.getBexpr() != null) { set.add(BEXPR_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiParenBexpr
            , tmplt: PsiTemplateGen<PsiParenBexpr, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiParenBexpr): PsiTemplateGen<PsiParenBexpr, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addBexprToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}