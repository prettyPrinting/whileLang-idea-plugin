package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiWhileStmt
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


public class WhileStmtComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiWhileStmt, SmartInsertPlace, PsiTemplateGen<PsiWhileStmt, SmartInsertPlace>>(printer)
   
{

    final val CONDITION_TAG: String
        get() = "condition"
    final val STMTLIST_TAG: String
        get() = "stmtlist"
    
    private fun addConditionToInsertPlaceMap(
            p: PsiWhileStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val condition = p.getBexpr()
        val conditionTextRange = condition?.getTextRange()
        if (conditionTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(conditionTextRange)
        
        insertPlaceMap.put(
               CONDITION_TAG
               , SmartInsertPlace(conditionTextRange.shiftRight(delta), fillConstant, condition!!.toBox())
            )
        return true
    }
    
    private fun prepareConditionVariants(
            p: PsiWhileStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val conditionVariants = getConditionVariants(p, context)
        if (conditionVariants.isEmpty()) { return }
        variants.put(CONDITION_TAG, conditionVariants)
    }
    
    private fun getConditionVariants(
            p: PsiWhileStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val condition = p.getBexpr()
        if (condition == null) { return printer.getEmptySet() }
        return printer.getVariants(condition, context)
    }
    
    private fun addStmtListToInsertPlaceMap(
            p: PsiWhileStmt
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
            p: PsiWhileStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val stmtListVariants = getStmtListVariants(p, context)
        if (stmtListVariants.isEmpty()) { return }
        variants.put(STMTLIST_TAG, stmtListVariants)
    }
    
    private fun getStmtListVariants(
            p: PsiWhileStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val stmtList = p.getStmtList()
        if (stmtList == null) { return printer.getEmptySet() }
        return printer.getVariants(stmtList, context)
    }
    
    
    override protected fun getNewElement(
            text: String
    ): PsiWhileStmt? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
            val newP = elementFactory.createWhileStmtFromText(text)
            return newP as? PsiWhileStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiWhileStmt
            , tmplt   : PsiTemplateGen<PsiWhileStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiWhileStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareConditionVariants(p, variants, context)
        prepareStmtListVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiWhileStmt): Set<String> {
        val set = HashSet<String>()
    
        if (p.getBexpr() != null) { set.add(CONDITION_TAG) }
        if (p.getStmtList() != null) { set.add(STMTLIST_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiWhileStmt
            , tmplt: PsiTemplateGen<PsiWhileStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiWhileStmt): PsiTemplateGen<PsiWhileStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addConditionToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        if (!addStmtListToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}