package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.psi.impl.PsiAssignStmt


public class AssignStmtComponent(
        printer: Printer
): PsiElementComponent<PsiAssignStmt, SmartInsertPlace, PsiTemplateGen<PsiAssignStmt, SmartInsertPlace>>(printer)
   
{

    final val VARIABLE_TAG: String
        get() = "variable"
    final val EXPRESSION_TAG: String
        get() = "expression"
    
    private fun addVariableToInsertPlaceMap(
            p: PsiAssignStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val variable = p.getId()
        val variableTextRange = variable?.getTextRange()
        if (variableTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(variableTextRange)
        
        insertPlaceMap.put(
               VARIABLE_TAG
               , SmartInsertPlace(variableTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareVariableVariants(
            p: PsiAssignStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val variableVariants = getVariableVariants(p, context)
        if (variableVariants.isEmpty()) { return }
        variants.put(VARIABLE_TAG, variableVariants)
    }
    
    private fun getVariableVariants(
            p: PsiAssignStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val variable = p.getId()
        if (variable == null) { return printer.getEmptySet() }
        return printer.getVariants(variable, context)
    }
    
    private fun addExpressionToInsertPlaceMap(
            p: PsiAssignStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val expression = p.getExpr()
        val expressionTextRange = expression?.getTextRange()
        if (expressionTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(expressionTextRange)
        
        insertPlaceMap.put(
               EXPRESSION_TAG
               , SmartInsertPlace(expressionTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareExpressionVariants(
            p: PsiAssignStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val expressionVariants = getExpressionVariants(p, context)
        if (expressionVariants.isEmpty()) { return }
        variants.put(EXPRESSION_TAG, expressionVariants)
    }
    
    private fun getExpressionVariants(
            p: PsiAssignStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val expression = p.getExpr()
        if (expression == null) { return printer.getEmptySet() }
        return printer.getVariants(expression, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: PsiElementFactory
    ): PsiAssignStmt? {
        try {
            val newP = elementFactory.createStatementFromText(text, null)
            return newP as? PsiAssignStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiAssignStmt
            , tmplt   : PsiTemplateGen<PsiAssignStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiAssignStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareVariableVariants(p, variants, context)
        prepareExpressionVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiAssignStmt): Set<String> {
        val set = HashSet<String>()
    
        if (p.getId() != null) { set.add(VARIABLE_TAG) }
        if (p.getExpr() != null) { set.add(EXPRESSION_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiAssignStmt
            , tmplt: PsiTemplateGen<PsiAssignStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiAssignStmt): PsiTemplateGen<PsiAssignStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        addVariableToInsertPlaceMap(newP, insertPlaceMap, negShift)
        addExpressionToInsertPlaceMap(newP, insertPlaceMap, negShift)
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}