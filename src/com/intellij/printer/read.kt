package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.psi.impl.PsiReadStmt


public class ReadStmtComponent(
        printer: Printer
): PsiElementComponent<PsiReadStmt, SmartInsertPlace, PsiTemplateGen<PsiReadStmt, SmartInsertPlace>>(printer)
   
{

    final val VARIABLE_TAG: String
        get() = "variable"
    
    private fun addVariableToInsertPlaceMap(
            p: PsiReadStmt
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
               , SmartInsertPlace(variableTextRange.shiftRight(delta), fillConstant, variable!!.toBox())
            )
        return true
    }
    
    private fun prepareVariableVariants(
            p: PsiReadStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val variableVariants = getVariableVariants(p, context)
        if (variableVariants.isEmpty()) { return }
        variants.put(VARIABLE_TAG, variableVariants)
    }
    
    private fun getVariableVariants(
            p: PsiReadStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val variable = p.getId()
        if (variable == null) { return printer.getEmptySet() }
        return printer.getVariants(variable, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: PsiElementFactory
    ): PsiReadStmt? {
        try {
            val newP = elementFactory.createStatementFromText(text, null)
            return newP as? PsiReadStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiReadStmt
            , tmplt   : PsiTemplateGen<PsiReadStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiReadStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareVariableVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiReadStmt): Set<String> {
        val set = HashSet<String>()
    
        if (p.getId() != null) { set.add(VARIABLE_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiReadStmt
            , tmplt: PsiTemplateGen<PsiReadStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiReadStmt): PsiTemplateGen<PsiReadStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addVariableToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}