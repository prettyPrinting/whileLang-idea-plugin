package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.psi.impl.PsiIfStmt


public class IfStmtComponent(
        printer: Printer
): PsiElementComponent<PsiIfStmt, SmartInsertPlace, PsiTemplateGen<PsiIfStmt, SmartInsertPlace>>(printer)
   
{

    final val THEN_TAG: String
        get() = "then"
    final val ELSE_TAG: String
        get() = "else"
    final val CONDITION_TAG: String
        get() = "condition"
    
    private fun addConditionToInsertPlaceMap(
            p: PsiIfStmt
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
            p: PsiIfStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val conditionVariants = getConditionVariants(p, context)
        if (conditionVariants.isEmpty()) { return }
        variants.put(CONDITION_TAG, conditionVariants)
    }
    
    private fun getConditionVariants(
            p: PsiIfStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val condition = p.getBexpr()
        if (condition == null) { return printer.getEmptySet() }
        return printer.getVariants(condition, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: PsiElementFactory
    ): PsiIfStmt? {
        try {
            val newP = elementFactory.createStatementFromText(text, null)
            return newP as? PsiIfStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiIfStmt
            , tmplt   : PsiTemplateGen<PsiIfStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiIfStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        preparePossibleCodeBlockPart(p.getThenBranch(), THEN_TAG, variants, context)
        preparePossibleCodeBlockPart(p.getElseBranch(), ELSE_TAG, variants, context)
        prepareConditionVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiIfStmt): Set<String> {
        val set = HashSet<String>()
    
        addPossibleCodeBlockTag(set, p.getThenBranch(), THEN_TAG)
        addPossibleCodeBlockTag(set, p.getElseBranch(), ELSE_TAG)
        if (p.getBexpr() != null) { set.add(CONDITION_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiIfStmt
            , tmplt: PsiTemplateGen<PsiIfStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiIfStmt): PsiTemplateGen<PsiIfStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addCBtoInsertPlaceMap(newP.getThenBranch(), THEN_TAG, insertPlaceMap, text)) { return null }
        addCBtoInsertPlaceMap(newP.getElseBranch(), ELSE_TAG, insertPlaceMap, text)
        if (!addConditionToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}