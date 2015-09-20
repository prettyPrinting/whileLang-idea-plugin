package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiProcList
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.util.box.Box
import org.jetbrains.prettyPrinter.core.util.psiElement.getCorrectTextOffset
import org.jetbrains.prettyPrinter.core.util.psiElement.getTextRange
import org.jetbrains.prettyPrinter.core.util.string.getFillConstant
import java.util.HashMap
import java.util.HashSet


public class ProcListComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiProcList, SmartInsertPlace, PsiTemplateGen<PsiProcList, SmartInsertPlace>>(printer)
   
{

    final val LIST_TAG: String
        get() = "list"
    
    private fun addListToInsertPlaceMap(
            p: PsiProcList
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val list = p.getProcedureList()
        val listTextRange = list?.getTextRange()
        if (listTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(listTextRange)
        
        insertPlaceMap.put(
               LIST_TAG
               , SmartInsertPlace(listTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareListVariants(
            p: PsiProcList
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val listVariants = getListVariants(p, context)
        if (listVariants.isEmpty()) { return }
        variants.put(LIST_TAG, listVariants)
    }
    
    private fun getListVariants(
            p: PsiProcList
            , context: VariantConstructionContext
    ): FormatSet {
        val list = p.getProcedureList()
        if (list == null || list.isEmpty()) { return printer.getEmptySet() }
        
        val listVariants = list.map { e -> printer.getVariants(e, context) }
        val variants = listVariants.fold(printer.getInitialSet(), {r, e -> r - e})
        return variants
    }
    
    
    override protected fun getNewElement(
            text: String
    ): PsiProcList? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
            val newP = elementFactory.createProcListFromText(text)
            return newP as? PsiProcList
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiProcList
            , tmplt   : PsiTemplateGen<PsiProcList, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiProcList
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareListVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiProcList): Set<String> {
        val set = HashSet<String>()
    
        if (p.getProcedureList() != null && !p.getProcedureList().isEmpty()) { set.add(LIST_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiProcList
            , tmplt: PsiTemplateGen<PsiProcList, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiProcList): PsiTemplateGen<PsiProcList, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addListToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}