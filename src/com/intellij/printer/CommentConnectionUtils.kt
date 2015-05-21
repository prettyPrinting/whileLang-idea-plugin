package com.intellij

import com.intellij.psi.*
import org.jetbrains.format.Format
import org.jetbrains.format.FormatSet
import java.util.HashMap

/**
 * User: anlun
 */
public trait CommentConnectionUtils {
    public fun getMaxWidth(): Int
    public fun getInitialSet(f: Format = Format.empty): FormatSet

    public class CommentConnection(
      val contentBefore: FormatSet?
    , val contentAfter : FormatSet?
    )

    public class VariantConstructionContext(
      val commentContext: MutableMap<PsiElement, CommentConnection>
    , val widthToSuit   : WidthToSuit
    )

    public fun defaultContext(): VariantConstructionContext =
            VariantConstructionContext(HashMap(), SimpleWidthToSuit(getMaxWidth()))

    protected fun updateCommentConnection(context: CommentConnection?, update: CommentConnection?): CommentConnection? {
        if (context == null) { return  update }
        if ( update == null) { return context }

        val contextBefore = context.contentBefore
        val  updateBefore =  update.contentBefore
        val newBefore: FormatSet? = if (contextBefore != null)
                                         contextBefore - (updateBefore ?: getInitialSet())
                                    else updateBefore

        val contextAfter = context.contentAfter
        val  updateAfter =  update.contentAfter
        val newAfter: FormatSet? = if (contextAfter != null)
                                        contextAfter - (updateAfter ?: getInitialSet())
                                   else updateAfter

        if (newBefore == null && newAfter == null) { return null }
        return CommentConnection(newBefore, newAfter)
    }

    protected fun updateElementCommentConnection(
              p: PsiElement
            , newCommentConnection: CommentConnection?
            , context: MutableMap<PsiElement, CommentConnection>
    ) {
        val updatedConnection = updateCommentConnection(context.get(p), newCommentConnection)
        if (updatedConnection == null) { return }
        context.put(p, updatedConnection)
    }

    public fun surroundVariantsByAttachedComments(
                     p: PsiElement
            , variants: FormatSet
            ,  context: VariantConstructionContext
    ): FormatSet {
        val curCommentConnection = context.commentContext.get(p)
        if (curCommentConnection == null) { return variants }

        var result = variants
        val contentBefore = curCommentConnection.contentBefore
        if (contentBefore != null) { result = contentBefore - result }
        val contentAfter = curCommentConnection.contentAfter
        if (contentAfter != null) { result = result - contentAfter }

        return result
    }

    public fun getCommentContext(p: PsiElement): MutableMap<PsiElement, CommentConnection> {
        val context = HashMap<PsiElement, CommentConnection>()

        val children = p.getChildren()
        val meaningChildren = children.filterNot { child ->
            child is PsiWhiteSpace || child is PsiJavaToken || !(hasElement(child))
        } //TODO: may be need another filtering
        if (meaningChildren.isEmpty()) { return context }

        val firstCommentChildren = meaningChildren.takeWhile { c -> c is PsiComment }
        firstCommentChildren.map { c ->
            val variants = getVariantsByText(c)
            updateElementCommentConnection(p, CommentConnection(variants, null), context)
        }
        val  lastCommentChildren = meaningChildren.drop(firstCommentChildren.size).reverse().takeWhile { c -> c is PsiComment }
        lastCommentChildren.map { c ->
            val variants = getVariantsByText(c)
            updateElementCommentConnection(p, CommentConnection(null, variants), context)
        }

        val indices = meaningChildren.indices
                .drop(firstCommentChildren.size)
                .reverse()
                .drop( lastCommentChildren.size)
                .reverse()
        for (i in indices) {
            val child = meaningChildren[i]
            if (child !is PsiComment) { continue }

            val childVariants = getVariantsByText(child)

            if (i != meaningChildren.lastIndex) {
                val nextSibling = blockStatementToCodeBlock(meaningChildren[i + 1])
                val beforeContext = context.get(child)?.contentBefore
                val contentForNextSibling = if (beforeContext != null)
                                                beforeContext - childVariants
                                            else childVariants
                context.put(nextSibling, CommentConnection(contentForNextSibling, null))
                continue
            }

            if (i != 0) {
                val prevSibling = blockStatementToCodeBlock(meaningChildren[i - 1])
                val prevSiblingCommentConnection = context.get(prevSibling)
                context.put(prevSibling, CommentConnection(prevSiblingCommentConnection?.contentBefore, childVariants))
                continue
            }

            val connection = CommentConnection(childVariants, null)
            updateElementCommentConnection(p, connection, context)
        }

        return context
    }

    private fun blockStatementToCodeBlock(p: PsiElement): PsiElement =
            if (p is PsiBlockStatement) p.getCodeBlock() else p

    public fun getVariantsByText(p: PsiElement): FormatSet
}