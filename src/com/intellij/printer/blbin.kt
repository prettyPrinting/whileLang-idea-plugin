package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.psi.impl.PsiBinaryBexpr


public class BinaryBexprComponent(
        printer: Printer
): PsiElementComponent<PsiBinaryBexpr, SmartInsertPlace, PsiTemplateGen<PsiBinaryBexpr, SmartInsertPlace>>(printer)
   
{

    final val LEFTOP_TAG: String
        get() = "leftop"
    final val RIGHTOP_TAG: String
        get() = "rightop"
    final val OPERATION_TAG: String
        get() = "operation"
    
    private fun addLeftOpToInsertPlaceMap(
            p: PsiBinaryBexpr
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val leftOp = p.getLeft()
        val leftOpTextRange = leftOp?.getTextRange()
        if (leftOpTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(leftOpTextRange)
        
        insertPlaceMap.put(
               LEFTOP_TAG
               , SmartInsertPlace(leftOpTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareLeftOpVariants(
            p: PsiBinaryBexpr
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val leftOpVariants = getLeftOpVariants(p, context)
        if (leftOpVariants.isEmpty()) { return }
        variants.put(LEFTOP_TAG, leftOpVariants)
    }
    
    private fun getLeftOpVariants(
            p: PsiBinaryBexpr
            , context: VariantConstructionContext
    ): FormatSet {
        val leftOp = p.getLeft()
        if (leftOp == null) { return printer.getEmptySet() }
        return printer.getVariants(leftOp, context)
    }
    
    private fun addRightOpToInsertPlaceMap(
            p: PsiBinaryBexpr
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val rightOp = p.getRight()
        val rightOpTextRange = rightOp?.getTextRange()
        if (rightOpTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(rightOpTextRange)
        
        insertPlaceMap.put(
               RIGHTOP_TAG
               , SmartInsertPlace(rightOpTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareRightOpVariants(
            p: PsiBinaryBexpr
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val rightOpVariants = getRightOpVariants(p, context)
        if (rightOpVariants.isEmpty()) { return }
        variants.put(RIGHTOP_TAG, rightOpVariants)
    }
    
    private fun getRightOpVariants(
            p: PsiBinaryBexpr
            , context: VariantConstructionContext
    ): FormatSet {
        val rightOp = p.getRight()
        if (rightOp == null) { return printer.getEmptySet() }
        return printer.getVariants(rightOp, context)
    }
    
    private fun addOperationToInsertPlaceMap(
            p: PsiBinaryBexpr
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val operation = p.getBlOp()
        val operationTextRange = operation?.getTextRange()
        if (operationTextRange == null) { return false }
        
        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(operationTextRange)
        
        insertPlaceMap.put(
               OPERATION_TAG
               , SmartInsertPlace(operationTextRange.shiftRight(delta), fillConstant, Box.getEverywhereSuitable())
            )
        return true
    }
    
    private fun prepareOperationVariants(
            p: PsiBinaryBexpr
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val operationVariants = getOperationVariants(p, context)
        if (operationVariants.isEmpty()) { return }
        variants.put(OPERATION_TAG, operationVariants)
    }
    
    private fun getOperationVariants(
            p: PsiBinaryBexpr
            , context: VariantConstructionContext
    ): FormatSet {
        val operation = p.getBlOp()
        if (operation == null) { return printer.getEmptySet() }
        return printer.getVariants(operation, context)
    }
    
    
    override protected fun getNewElement(
            text: String
            , elementFactory: PsiElementFactory
    ): PsiBinaryBexpr? {
        try {
            val newP = elementFactory.createBexpressionFromText(text, null)
            return newP as? PsiBinaryBexpr
        } catch (e: Exception) {
            return null
        }
    }

    override protected fun updateSubtreeVariants(
            p       : PsiBinaryBexpr
            , tmplt   : PsiTemplateGen<PsiBinaryBexpr, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: PsiBinaryBexpr
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        val variants = HashMap<String, FormatSet>()
    
        prepareLeftOpVariants(p, variants, context)
        prepareRightOpVariants(p, variants, context)
        prepareOperationVariants(p, variants, context)
        
        
    
        return variants
    }

    override protected fun getTags(p: PsiBinaryBexpr): Set<String> {
        val set = HashSet<String>()
    
        if (p.getLeft() != null) { set.add(LEFTOP_TAG) }
        if (p.getRight() != null) { set.add(RIGHTOP_TAG) }
        if (p.getBlOp() != null) { set.add(OPERATION_TAG) }
        
        
    
        return set
    }

    override protected fun isTemplateSuitable(
            p: PsiBinaryBexpr
            , tmplt: PsiTemplateGen<PsiBinaryBexpr, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: PsiBinaryBexpr): PsiTemplateGen<PsiBinaryBexpr, SmartInsertPlace>? {
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        val negShift = -newP.getCorrectTextOffset()
    
        val text = newP.getText() ?: ""
    
        if (!addLeftOpToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        if (!addRightOpToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        if (!addOperationToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        
        
    
        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    
}