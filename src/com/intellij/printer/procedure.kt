package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.psi.impl.PsiProcedure


public class ProcedureComponent(
        printer: Printer
): PsiElementComponent<PsiProcedure, SmartInsertPlace, PsiTemplateGen<PsiProcedure, SmartInsertPlace>>(printer)
   
{

    final val PROCNAME_TAG: String
        get() = "procname"
    final val STMTLIST_TAG: String
        get() = "stmtlist"
    
    private fun addProcNameToInsertPlaceMap(
            p: PsiProcedure
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val procName = p.getId()
        val procNameTextRange = procName?.getTextRange()
        if (procNameTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(procNameTextRange)
        
        insertPlaceMap.put(
               PROCNAME_TAG
               , SmartInsertPlace(procNameTextRange.shiftRight(delta), fillConstant, procName!!.toBox())
            )
        return true
    }
    
    private fun prepareProcNameVariants(
            p: PsiProcedure
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val procNameVariants = getProcNameVariants(p, context)
        if (procNameVariants.isEmpty()) { return }
        variants.put(PROCNAME_TAG, procNameVariants)
    }
    
    private fun getProcNameVariants(
            p: PsiProcedure
            , context: VariantConstructionContext
    ): FormatSet {
        val procName = p.getId()
        if (procName == null) { return printer.getEmptySet() }
        return printer.getVariants(procName, context)
    }
    
    private fun addStmtListToInsertPlaceMap(
            p: PsiProcedure
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val stmtList = p.getStmtList()
        val stmtListTextRange = stmtList?.getTextRange()
        if (stmtListTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(stmtListTextRange)
        
        insertPlaceMap.put(
               STMTLIST_TAG
               , SmartInsertPlace(stmtListTextRange.shiftRight(delta), fillConstant, stmtList!!.toBox())
            )
        return true
    }
    
    private fun prepareStmtListVariants(
            p: PsiProcedure
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val stmtListVariants = getStmtListVariants(p, context)
        if (stmtListVariants.isEmpty()) { return }
        variants.put(STMTLIST_TAG, stmtListVariants)
    }
    
    private fun getStmtListVariants(
            p: PsiProcedure
            , context: VariantConstructionContext
    ): FormatSet {
        val stmtList = p.getStmtList()
        if (stmtList == null) { return printer.getEmptySet() }
        return printer.getVariants(stmtList, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: WhileElementFactory
    ): PsiProcedure? {
        try {
            val newP = elementFactory.createProcedureFromText(text)
            return newP as? PsiProcedure
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiProcedure
            , tmplt   : PsiTemplateGen<PsiProcedure, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiProcedure
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareProcNameVariants(p, variants, context)
        prepareStmtListVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiProcedure): Set<String> {
        val set = HashSet<String>()
    
        if (p.getId() != null) { set.add(PROCNAME_TAG) }
        if (p.getStmtList() != null) { set.add(STMTLIST_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiProcedure
            , tmplt: PsiTemplateGen<PsiProcedure, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiProcedure): PsiTemplateGen<PsiProcedure, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addProcNameToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        if (!addStmtListToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}