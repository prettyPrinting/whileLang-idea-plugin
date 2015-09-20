package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.psi.impl.PsiWriteStmt
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


public class WriteStmtComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<PsiWriteStmt, SmartInsertPlace, PsiTemplateGen<PsiWriteStmt, SmartInsertPlace>>(printer)
   
{

    final val EXPR_TAG: String
        get() = "expr"
    
    private fun addExprToInsertPlaceMap(
            p: PsiWriteStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val expr = p.getExpr()
        val exprTextRange = expr?.getTextRange()
        if (exprTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(exprTextRange)
        
        insertPlaceMap.put(
               EXPR_TAG
               , SmartInsertPlace(exprTextRange.shiftRight(delta), fillConstant, expr!!.toBox())
            )
        return true
    }
    
    private fun prepareExprVariants(
            p: PsiWriteStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val exprVariants = getExprVariants(p, context)
        if (exprVariants.isEmpty()) { return }
        variants.put(EXPR_TAG, exprVariants)
    }
    
    private fun getExprVariants(
            p: PsiWriteStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val expr = p.getExpr()
        if (expr == null) { return printer.getEmptySet() }
        return printer.getVariants(expr, context)
    }
    
    
    override protected fun getNewElement(
            text: String
    ): PsiWriteStmt? {
        try {
            val elementFactory  = WhileElementFactory(printer.getProject())
            val newP = elementFactory.createWriteStmtFromText(text)
            return newP as? PsiWriteStmt
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiWriteStmt
            , tmplt   : PsiTemplateGen<PsiWriteStmt, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiWriteStmt
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareExprVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiWriteStmt): Set<String> {
        val set = HashSet<String>()
    
        if (p.getExpr() != null) { set.add(EXPR_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiWriteStmt
            , tmplt: PsiTemplateGen<PsiWriteStmt, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiWriteStmt): PsiTemplateGen<PsiWriteStmt, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addExprToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}