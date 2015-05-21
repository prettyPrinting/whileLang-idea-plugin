package com.intellij

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import org.jetbrains.format.FormatSet
import org.jetbrains.likePrinter.util.box.Box
import org.jetbrains.likePrinter.util.box.toBox
import org.jetbrains.likePrinter.util.string.*

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
    val children = getChildren()
    if (children.isEmpty()) { return getTextOffset() }

    if (!withoutLeadingComments) { return children[0].getCorrectTextOffset() }

    for (ch in getChildren()) {
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

fun PsiElement.getTextWithoutLeadingComments(): String {
    for (ch in getChildren()) {
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