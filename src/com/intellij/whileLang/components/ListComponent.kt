package com.intellij.whileLang.components

import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.format.Format
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.templateBase.PsiElementComponent
import org.jetbrains.prettyPrinter.core.templateBase.template.PsiTemplateGen
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.templateBase.template.Template
import org.jetbrains.prettyPrinter.core.util.box.Box
import org.jetbrains.prettyPrinter.core.util.psiElement.getCorrectTextOffset
import org.jetbrains.prettyPrinter.core.util.psiElement.getFillConstant
import org.jetbrains.prettyPrinter.core.util.psiElement.getNotNullTextRange
import org.jetbrains.prettyPrinter.core.util.psiElement.getOffsetInStartLine
import org.jetbrains.prettyPrinter.core.util.string.*
import java.util.ArrayList
import java.util.HashMap

/**
 * User: anlun
 */

public class ListTemplate<ET: PsiElement, IPT: SmartInsertPlace>(
  p: ET
, insertPlaceMap: Map<String, IPT>
, tagPlaceToLineNumberMap: Map<TagPlaceLine, Int>
, lineEquationMap        : Map<Int, LineEquation>
): PsiTemplateGen<ET, IPT>(p, insertPlaceMap, tagPlaceToLineNumberMap, lineEquationMap)

public interface ListComponent<ET: PsiElement>: PsiElementComponent<ET, SmartInsertPlace, ListTemplate<ET, SmartInsertPlace>> {
    override protected fun getNewElement(text: String): ET? = null
    override public    fun getTemplateFromElement(newP: ET): ListTemplate<ET, SmartInsertPlace>? = null

    final val ELEMENTS_TAG: String
        get() = "elements"

    override public fun getTmplt(p: ET): ListTemplate<ET, SmartInsertPlace>? {
        val newP = getNormalizedElement(p) ?: p
        return getListTmplt(newP)
    }

    open public fun getNormalizedElement(p: ET): ET? = null

    public fun getListElement(text: String): ET?

    /// Deletes indentation //TODO
//    public fun getNormalizedList(p: ET): ET?

    protected fun getListTmplt(
              p: ET
    ): ListTemplate<ET, SmartInsertPlace>? {
        for (ch in p.getChildren()) {
            if (ch is PsiComment) {
                return null
            }
        }

        val list = getList(p)
        if (list.isEmpty()) { return null }

        val firstElem  = list[0]
        val lastElem   = list[list.lastIndex]

        val firstRange = firstElem.getNotNullTextRange()
        val lastRange  = lastElem .getNotNullTextRange()

        val rangeInFile = firstRange.union(lastRange)
        val text = p.getContainingFile()?.getText() ?: ""
        val fillConstant = text.getFillConstant(rangeInFile)
        val range = rangeInFile.shiftRight(-p.getCorrectTextOffset())
        val insertPlaceMap = HashMap<String, SmartInsertPlace>()
        insertPlaceMap.put(ELEMENTS_TAG, SmartInsertPlace(range, fillConstant, Box.getEverywhereSuitable()))

        val contentRelation = getContentRelation(p.getText() ?: "", insertPlaceMap)
        return ListTemplate(p, insertPlaceMap, contentRelation.first, contentRelation.second)
    }

    final val ELEMENT_VARIANTS_TAG: String
        get() = "element variants"

    protected fun getList(p: ET): List<PsiElement>

    override protected fun prepareSubtreeVariants(p: ET, context: VariantConstructionContext): Map<String, FormatSet> {
        /*
        val variants = HashMap<String, FormatSet>()
        val list     = getList(p)

        var counter = 0
        list.map { e -> variants.put(ELEMENT_VARIANTS_TAG + counter, printer.getVariants(e, context)); counter++ }

        return variants
        */
        return mapOf(Pair(ELEMENTS_TAG, getElementsVariants_new(p, context)))
    }
    override protected fun updateSubtreeVariants(
              p       : ET
            , tmplt   : ListTemplate<ET, SmartInsertPlace>
            , variants: Map<String, FormatSet>
            ,  context: VariantConstructionContext
    ): Map<String, FormatSet>  = variants /*{

        /*
        val list = getList(p)
        if (list.isEmpty()) { return mapOf(Pair(ELEMENTS_TAG, printer.getInitialSet())) }

        val elemVariantsArray = Array<FormatSet>(list.size) { i ->
            val curElemVariants = variants.get(ELEMENT_VARIANTS_TAG + i)
            curElemVariants ?: printer.getEmptySet()
        }

        val listElem2Tmplt = tmplt.listElem2Tmplt
        var elemListVariants = elemVariantsArray[elemVariantsArray.lastIndex]
        for (i in (list.lastIndex - 1) downTo 0) {
            elemListVariants = listElem2Tmplt.flatMap { t ->
                getVariants(
                          t.text
                        , t.insertPlaceMap
                        , mapOf(Pair(FIRST_ELEMENT_TAG, elemVariantsArray[i]), Pair(SECOND_ELEMENT_TAG, elemListVariants))
                ) ?: printer.getEmptySet()
            }
            //TODO: may be remove some variants from elemListVariants
            elemListVariants = elemListVariants.filter(printer.filterFmt(context.widthToSuit.getSuitWidth()))
        }

        return mapOf(Pair(ELEMENTS_TAG, elemListVariants))
        */
        return mapOf(Pair(ELEMENTS_TAG, getElementsVariantsByTemplate(p, tmplt, context)))
    }
    */

    protected fun getElementsVariants_new(
                    p: ET
            , context: VariantConstructionContext
    ): FormatSet = getElementsVariants(p, context, getSeparator_1())
    open public fun getSeparator_1(): Format = Format.line(", ")

    protected fun getElementsVariants(
                      p: ET
            ,   context: VariantConstructionContext
            , separator: Format
    ): FormatSet = getElementsVariants(getList(p), context, { _ -> separator })

    override protected fun getTags(p: ET) = setOf(ELEMENTS_TAG)
}