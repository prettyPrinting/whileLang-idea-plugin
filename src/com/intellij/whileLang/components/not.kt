package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiNotBexpr
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.util.box.Box
import org.jetbrains.prettyPrinter.core.util.psiElement.getCorrectTextOffset
import org.jetbrains.prettyPrinter.core.util.string.getFillConstant
import java.util.HashMap
import java.util.HashSet


public class NotBexprComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiNotBexpr, SmartInsertPlace, PsiTemplateGen<PsiNotBexpr, SmartInsertPlace>>(printer)
   
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
    ): PsiNotBexpr? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
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