package com.intellij;

import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.HashMap
import java.util.HashSet
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.psi.impl.PsiIfStmt


public class IfStmtComponent(
        printer: Printer
): PsiElementComponent<PsiIfStmt, SmartInsertPlace, PsiTemplateGen<PsiIfStmt, SmartInsertPlace>>(printer)

{

    final val THEN_TAG: String
        get() = "then"
    final val ELSE_BRANCH_TAG: String
        get() = "else_branch"
    final val CONDITION_TAG: String
        get() = "condition"

    private fun addThenToInsertPlaceMap(
            p: PsiIfStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val then = p.getThenBranch()
        val thenTextRange = then?.getTextRange()
        if (thenTextRange == null) { return false }

        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(thenTextRange)

        insertPlaceMap.put(
                THEN_TAG
                , SmartInsertPlace(thenTextRange.shiftRight(delta), fillConstant, then!!.toBox())
        )
        return true
    }

    private fun prepareThenVariants(
            p: PsiIfStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val thenVariants = getThenVariants(p, context)
        if (thenVariants.isEmpty()) { return }
        variants.put(THEN_TAG, thenVariants)
    }

    private fun getThenVariants(
            p: PsiIfStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val then = p.getThenBranch()
        if (then == null) { return printer.getEmptySet() }
        return printer.getVariants(then, context)
    }

    private fun addElse_branchToInsertPlaceMap(
            p: PsiIfStmt
            , insertPlaceMap: MutableMap<String, SmartInsertPlace>
            , delta: Int
    ): Boolean {
        val else_branch = p.getElseBranch()
        val else_branchTextRange = else_branch?.getTextRange()
        if (else_branchTextRange == null) { return false }

        val text = p.getContainingFile()?.getText()
        if (text == null) { return false }
        val fillConstant = text.getFillConstant(else_branchTextRange)

        insertPlaceMap.put(
                ELSE_BRANCH_TAG
                , SmartInsertPlace(else_branchTextRange.shiftRight(delta), fillConstant, else_branch!!.toBox())
        )
        return true
    }

    private fun prepareElse_branchVariants(
            p: PsiIfStmt
            , variants: MutableMap<String, FormatSet>
            , context: VariantConstructionContext
    ) {
        val else_branchVariants = getElse_branchVariants(p, context)
        if (else_branchVariants.isEmpty()) { return }
        variants.put(ELSE_BRANCH_TAG, else_branchVariants)
    }

    private fun getElse_branchVariants(
            p: PsiIfStmt
            , context: VariantConstructionContext
    ): FormatSet {
        val else_branch = p.getElseBranch()
        if (else_branch == null) { return printer.getEmptySet() }
        return printer.getVariants(else_branch, context)
    }

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
            , elementFactory: WhileElementFactory
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

        prepareThenVariants(p, variants, context)
        prepareElse_branchVariants(p, variants, context)
        prepareConditionVariants(p, variants, context)



        return variants
    }

    override protected fun getTags(p: PsiIfStmt): Set<String> {
        val set = HashSet<String>()

        if (p.getThenBranch() != null) { set.add(THEN_TAG) }
        if (p.getElseBranch() != null) { set.add(ELSE_BRANCH_TAG) }
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

        if (!addThenToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }
        addElse_branchToInsertPlaceMap(newP, insertPlaceMap, negShift)
        if (!addConditionToInsertPlaceMap(newP, insertPlaceMap, negShift)) { return null }



        val contentRelation = getContentRelation(newP.getText() ?: "", insertPlaceMap)
        return PsiTemplateGen(newP, insertPlaceMap, contentRelation.first, contentRelation.second)
    }


}