package com.intellij.whileLang.components;

import com.intellij.whileLang.WhileFile
import com.intellij.whileLang.printer.WhilePrinter
import com.intellij.whileLang.templateBase.WhilePsiElementComponent
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

public class WhileFileComponent(
        printer: WhilePrinter
): WhilePsiElementComponent<WhileFile, SmartInsertPlace, PsiTemplateGen<WhileFile, SmartInsertPlace>>(printer)

{
    private fun getModuleVariants(
            p: WhileFile
            , context: VariantConstructionContext
    ): FormatSet {
        val module = p.getStmtList()
        if (module == null) { return printer.getEmptySet() }
        return printer.getVariants(module, context)
    }

    private fun getProcListVariants(
            p: WhileFile
            , context: VariantConstructionContext
    ): FormatSet {
        val proc = p.getProcList()
        if (proc == null) { return printer.getEmptySet() }
        return  printer.getVariants(proc, context)
    }

    override public fun getVariants(p: WhileFile, context: VariantConstructionContext): FormatSet {
        val list = ArrayList<FormatSet>()

        val procListVariants = getProcListVariants(p, context)
        if (procListVariants != null) {
            if (procListVariants.isEmpty()) { return printer.getEmptySet() }
            list.add(procListVariants)
        }

        val moduleVariants = getModuleVariants(p, context)
        if (moduleVariants != null) {
            if (moduleVariants.isEmpty()) { return printer.getEmptySet() }
            list.add(moduleVariants)
        }

        val firstElem = list.get(0)

        return list.drop(1).fold(firstElem) { r, e -> r % e }
    }

    override public fun getTmplt(p: WhileFile): PsiTemplateGen<WhileFile, SmartInsertPlace>? {
        return getTemplateFromElement(p)
    }

    override protected fun getNewElement(
            text: String
    ): WhileFile? {
        return null
    }

    override protected fun updateSubtreeVariants(
            p       : WhileFile
            , tmplt   : PsiTemplateGen<WhileFile, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            , context: VariantConstructionContext
    ): Map<String, FormatSet> {
        return variants
    }

    override protected fun prepareSubtreeVariants(
            p: WhileFile
            , context: VariantConstructionContext
    ): Map<String, FormatSet> = HashMap()

    override protected fun getTags(p: WhileFile): Set<String>  = HashSet()

    override protected fun isTemplateSuitable(
            p: WhileFile
            , tmplt: PsiTemplateGen<WhileFile, SmartInsertPlace>
    ): Boolean {
        return true
    }

    override public fun getTemplateFromElement(newP: WhileFile): PsiTemplateGen<WhileFile, SmartInsertPlace>? = null
}
