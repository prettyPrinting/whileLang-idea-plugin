package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiProcedure
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.util.psiElement.getCorrectTextOffset
import org.jetbrains.prettyPrinter.core.util.psiElement.toBox
import org.jetbrains.prettyPrinter.core.util.string.getFillConstant
import java.util.HashMap
import java.util.HashSet


public class ProcedureComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiProcedure, SmartInsertPlace, PsiTemplateGen<PsiProcedure, SmartInsertPlace>>(printer)
   
{

    final val PROCNAME_TAG: String
        get() = "procname"
    final val PARAMLIST_TAG: String
        get() = "paramlist"
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
    
    private fun addParamListToInsertPlaceMap(
            p: PsiProcedure
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val paramList = p.getParamList()
        val paramListTextRange = paramList?.getTextRange()
        if (paramListTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(paramListTextRange)
        
        insertPlaceMap.put(
               PARAMLIST_TAG
               , SmartInsertPlace(paramListTextRange.shiftRight(delta), fillConstant, paramList!!.toBox())
            )
        return true
    }
    
    private fun prepareParamListVariants(
            p: PsiProcedure
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val paramListVariants = getParamListVariants(p, context)
        if (paramListVariants.isEmpty()) { return }
        variants.put(PARAMLIST_TAG, paramListVariants)
    }
    
    private fun getParamListVariants(
            p: PsiProcedure
            , context: VariantConstructionContext
    ): FormatSet {
        val paramList = p.getParamList()
        if (paramList == null) { return printer.getEmptySet() }
        return printer.getVariants(paramList, context)
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
    ): PsiProcedure? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
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
        prepareParamListVariants(p, variants, context)
        prepareStmtListVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiProcedure): Set<String> {
        val set = HashSet<String>()
    
        if (p.getId() != null) { set.add(PROCNAME_TAG) }
        if (p.getParamList() != null) { set.add(PARAMLIST_TAG) }
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
        addParamListToInsertPlaceMap(newP, insertPlaceMap, negShift)
        if (!addStmtListToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}