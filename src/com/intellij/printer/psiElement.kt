package com.intellij

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierListOwner
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.PsiComment

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiBlockStatement
import com.intellij.psi.PsiStatement
import com.intellij.whileLang.Printer
import org.jetbrains.format.FormatSet
import java.util.ArrayList
import java.util.LinkedList
import com.intellij.CommentConnectionUtils.VariantConstructionContext

/**
 * User: anlun
 */

fun hasElement(p: PsiElement?): Boolean = (p?.getTextRange()?.getLength() ?: -1) > 0

fun List<PsiElement>.sortByOffset(): List<PsiElement> = sortBy { el -> el.getCorrectTextOffset() }

fun Iterable<PsiElement>.getTextRange(): TextRange? =
        fold(null: TextRange?) { range, elem ->
            val elemRange = elem.getTextRange()
            if (range != null && elemRange != null)
                range.union(elemRange)
            else elemRange
        }
// Separate realization for arrays, because they don't implement Iterable interface
fun Array<out PsiElement>.getTextRange(): TextRange? =
        fold(null: TextRange?) { range, elem ->
            val elemRange = elem.getTextRange()
            if (range != null && elemRange != null)
                range.union(elemRange)
            else elemRange
        }

fun PsiElement?.getVariants(printer: Printer, context: VariantConstructionContext): FormatSet =
        if (this != null)
            printer.getVariants(this, context)
        else printer.getEmptySet()

fun PsiElement?.getNotNullTextRange(): TextRange = this?.getTextRange() ?: TextRange(0, 0)

/* ----------- */
fun PsiElement.getCorrectTextOffset(): Int = getCorrectTextOffset(false)
fun PsiElement.getCorrectTextOffset(withoutLeadingComments: Boolean): Int {
    val children = getAllChildren()
    if (children.isEmpty()) { return getTextOffset() }

    if (!withoutLeadingComments) { return children[0].getCorrectTextOffset() }

    for (ch in children) {
        if (ch is PsiWhiteSpace || ch is PsiComment) { continue }
        return ch.getCorrectTextOffset(withoutLeadingComments)
    }

    return getTextOffset()
}

fun PsiElement.getOffsetInStartLine(): Int = getOffsetInStartLine(false)
fun PsiElement.getOffsetInStartLine(withoutLeadingComments: Boolean): Int {
    val offset = getCorrectTextOffset(withoutLeadingComments)
    val text = getContainingFile()?.getText() ?: ""
    return text.getOffsetInLine(offset)
}

fun PsiElement.getFillConstant(): Int {
    val range = getNotNullTextRange()
    val text  = getContainingFile()?.getText() ?: ""
    return text.getFillConstant(range)
}

/* ----------- */

fun PsiElement.maxDropSpaceNumber(): Int = maxDropSpaceNumber(false)
fun PsiElement.maxDropSpaceNumber(withoutLeadingComments: Boolean): Int {
    val startLineOffset = getOffsetInStartLine(withoutLeadingComments)
    return getText()?.maxDropSpaceNumber(startLineOffset) ?: 0
}

fun PsiElement.doesStartWithNewLine(): Boolean {
    val offset = getCorrectTextOffset()
    val text   = getContainingFile()?.getText() ?: ""
    return text.isLineStart(offset)
}

// Deleting unneeded spaces. DON'T work with tabs
fun PsiElement.deleteSpaces(): String = deleteSpaces(false)
fun PsiElement.deleteSpaces(withoutLeadingComments: Boolean): String {
    val lenToDrop = maxDropSpaceNumber(withoutLeadingComments)
    val text = getText(withoutLeadingComments)
    return text.deleteSpacesAfterFirstLine(lenToDrop)
}

fun PsiElement.getText(withoutLeadingComments: Boolean): String {
    if (withoutLeadingComments) { return getTextWithoutLeadingComments() }
    return getText() ?: ""
}

fun PsiElement.getAllChildren() : List<PsiElement> {
    val result = LinkedList<PsiElement>()
    var child = getFirstChild()
    while (child != null) {
        result.add(child)
        child = child.getNextSibling()
    }
    return result
}

fun PsiElement.getTextWithoutLeadingComments(): String {
    val children = getAllChildren()
    for (ch in children) {
        if (ch is PsiWhiteSpace || ch is PsiComment) { continue }

        //ch isn't whitespace or comment
        val startOffset = ch.getTextOffset()
        return getContainingFile()?.getText()
                ?.substring(startOffset, getTextRange()?.getEndOffset() ?: startOffset)
                ?: ""
    }
    return getText() ?: ""
}

fun PsiElement.toSmartInsertPlace(): SmartInsertPlace {
    return SmartInsertPlace(getNotNullTextRange(), getFillConstant(), toBox())
}

fun Iterable<PsiElement>.toSmartInsertPlace(text: String, delta: Int = 0): SmartInsertPlace? {
    val resultRange = getTextRange()?.shiftRight(delta)
    if (resultRange == null) { return null }
    val statementsFillConstant = text.getFillConstant(resultRange)
    val blockBox = text.substring(resultRange).toBox(statementsFillConstant)
    return SmartInsertPlace(resultRange, statementsFillConstant, blockBox)
}
// Separate realization for arrays, because they don't implement Iterable interface
fun Array<out PsiElement>.toSmartInsertPlace(text: String, delta: Int = 0): SmartInsertPlace? {
    val resultRange = getTextRange()?.shiftRight(delta)
    if (resultRange == null) { return null }
    val statementsFillConstant = text.getFillConstant(resultRange)
    val blockBox = text.substring(resultRange).toBox(statementsFillConstant)
    return SmartInsertPlace(resultRange, statementsFillConstant, blockBox)
}

fun PsiElement?.toCodeBlock(): PsiCodeBlock? =
        when (this) {
            is PsiBlockStatement -> this.getCodeBlock()
            is PsiCodeBlock      -> this
            else -> null
        }

fun PsiElement.getStatements(): Array<PsiStatement>? = toCodeBlock()?.getStatements()

fun PsiElement.toBox(): Box {
    val text = getText() ?: ""
    return text.toBox(getFillConstant())
}
