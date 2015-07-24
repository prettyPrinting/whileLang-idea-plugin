package com.intellij.whileLang;

import com.intellij.psi.*
import com.intellij.openapi.project.Project

import java.util.ArrayList
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.impl.DummyProject
import java.util.HashMap
import com.intellij.util.IncorrectOperationException
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.codeInsight.documentation.DocumentationManager
import org.jetbrains.format.FormatSet.FormatSetType
import java.lang.management.ManagementFactory
import com.intellij.openapi.command.WriteCommandAction
import org.jetbrains.format.Format
import org.jetbrains.format.FormatSet
import org.jetbrains.format.SteppedFormatMap
import org.jetbrains.format.FormatMap3D_AF
import org.jetbrains.format.FormatMap3D
import org.jetbrains.format.FormatMap2D_LL
import org.jetbrains.format.FormatMap1D
import org.jetbrains.format.FormatList
import org.jetbrains.likePrinter.printer.Memoization
import org.jetbrains.likePrinter.printer.PrinterSettings
import com.intellij.CommentConnectionUtils
import com.intellij.CommentConnectionUtils.VariantConstructionContext
import com.intellij.*
import com.intellij.whileLang.psi
import com.intellij.whileLang.psi.impl.*


class Printer(
  templateFile: WhileFile?
, private val settings: PrinterSettings
): Memoization(), CommentConnectionUtils {
    companion object {
        public fun create(templateFile: WhileFile, project: Project, width: Int): Printer =
                Printer(templateFile, PrinterSettings.createProjectSettings(width, project))
    }

    public fun hasToUseMultipleListElemVariants(): Boolean = settings.multipleListElemVariants
    public fun hasToUseMultipleExprStmtVariants(): Boolean = settings.multipleExprStmtVariants

    public fun setMultipleListElemVariantNeeds(f: Boolean) { settings.multipleListElemVariants = f }
    public fun setMultipleExprStmtVariantNeeds(f: Boolean) { settings.multipleExprStmtVariants = f }
    public fun setFormatSetType(f: FormatSetType) { settings.formatSetType = f }
    public fun setMaxWidth(f: Int) { settings.width = f }

    override public fun getMaxWidth(): Int = settings.width
    public fun    getProject(): Project   = settings.project
    public fun   getEmptySet(): FormatSet =
        when (settings.formatSetType) {
            FormatSetType.D1   -> FormatMap1D   (getMaxWidth())
            FormatSetType.D2   -> FormatMap2D_LL(getMaxWidth())
            FormatSetType.D3   -> FormatMap3D   (getMaxWidth())
            FormatSetType.D3AF -> FormatMap3D_AF(getMaxWidth())
            FormatSetType.List -> FormatList    (getMaxWidth())
            FormatSetType.SteppedD3AF -> SteppedFormatMap(FormatSet.stepInMap, getMaxWidth())

            else -> FormatMap3D(getMaxWidth())
        }
    override public fun getInitialSet(f: Format): FormatSet {
        val fs = getEmptySet()
        fs.add(f)
        return fs
    }

    //WARNING: must be declared before init!!!
    //COMPONENTS
    public val AssignStmtComponent: AssignStmtComponent = AssignStmtComponent(this)
    public val IfStmtComponent: IfStmtComponent = IfStmtComponent(this)
    public val ReadStmtComponent: ReadStmtComponent = ReadStmtComponent(this)
    public val SkipStmtComponent: SkipStmtComponent = SkipStmtComponent(this)
    public val WhileStmtComponent: WhileStmtComponent = WhileStmtComponent(this)
    public val StmtListComponent: StmtListComponent = StmtListComponent(this)
    public val WriteStmtComponent: WriteStmtComponent = WriteStmtComponent(this)
    public val BinaryExprComponent: BinaryExprComponent = BinaryExprComponent(this)
    public val ParenExprComponent: ParenExprComponent = ParenExprComponent(this)
    public val BinaryBexprComponent: BinaryBexprComponent = BinaryBexprComponent(this)
    public val ParenBexprComponent: ParenBexprComponent = ParenBexprComponent(this)
    public val NotBexprComponent: NotBexprComponent = NotBexprComponent(this)
    public val RelBexprComponent: RelBexprComponent = RelBexprComponent(this)
    public val ProcedureComponent: ProcedureComponent = ProcedureComponent(this)
    public val ProcListComponent: ProcListComponent = ProcListComponent(this)
    
    public val WhileFileComponent: WhileFileComponent = WhileFileComponent(this)

    public fun reprint(mFile: WhileFile) { reprintElementWithChildren(mFile) }

    init {
        if (templateFile != null) {
            fillTemplateLists(templateFile)
        }
    }

    /// public only for testing purposes!!!
    public fun reprintElementWithChildren(psiElement: PsiElement) {
        reprintElementWithChildren_AllMeaningful(psiElement) // variant for partial template
//        reprintElementWithChildren_OnlyWhileFile(psiElement) // variant for situations with full template
    }

    private fun reprintElementWithChildren_OnlyWhileFile(psiElement: PsiElement) {
        walker(psiElement) { p -> if (p is WhileFile) applyTmplt(p) }
    }

    private fun reprintElementWithChildren_AllMeaningful(psiElement: PsiElement) {
        walker(psiElement) { p ->
            when (p) {
                is PsiAssignStmt -> applyTmplt(p)
                is PsiIfStmt -> applyTmplt(p)
                is PsiReadStmt -> applyTmplt(p)
                is PsiSkipStmt -> applyTmplt(p)
                is PsiWhileStmt -> applyTmplt(p)
                is PsiStmtList -> applyTmplt(p)
                is PsiWriteStmt -> applyTmplt(p)
                is PsiBinaryExpr -> applyTmplt(p)
                is PsiParenExpr -> applyTmplt(p)
                is PsiBinaryBexpr -> applyTmplt(p)
                is PsiParenBexpr -> applyTmplt(p)
                is PsiNotBexpr -> applyTmplt(p)
                is PsiRelBexpr -> applyTmplt(p)
                is PsiProcedure -> applyTmplt(p)
                is PsiProcList -> applyTmplt(p)
                
                else -> 5 + 5
            }
        }
    }

    public fun getVariants(p: PsiElement, context: VariantConstructionContext = defaultContext()): FormatSet {
        val pCommentContext = getCommentContext(p)
        val widthToSuit = context.widthToSuit
        val variantConstructionContext = VariantConstructionContext(pCommentContext, widthToSuit)

        val mv = getMemoizedVariants(p)
        if (mv != null) { return surroundVariantsByAttachedComments(p, mv, context) }

        val resultWithoutOuterContextComments: FormatSet
        val templateVariant = getTemplateVariants(p, variantConstructionContext)
        if (templateVariant.isNotEmpty()) {
            resultWithoutOuterContextComments = surroundVariantsByAttachedComments(
                      p, templateVariant, variantConstructionContext
            )

            addToCache(p, resultWithoutOuterContextComments)
        } else {
            val s = p.getText() ?: ""
            if (s.contains(" ")) { log.add(s) }
            resultWithoutOuterContextComments = getVariantsByText(p)

            //TODO: For test purposes!!!
            addToCache(p, resultWithoutOuterContextComments)
        }

        val variants = surroundVariantsByAttachedComments(p, resultWithoutOuterContextComments, context)
        return variants
    }

    override public fun getVariantsByText(p: PsiElement): FormatSet {
        val offsetInStartLine = p.getOffsetInStartLine()
        val normalizedFillConstant = Math.max(p.getFillConstant(), 0)
        return getInitialSet(Format.text(p.getText(), offsetInStartLine + normalizedFillConstant))
    }

    private fun getTemplateVariants(p: PsiElement, context: VariantConstructionContext): FormatSet {
        val variants: FormatSet =
            when(p) {
                is PsiAssignStmt -> AssignStmtComponent.getVariants(p, context)
                is PsiIfStmt -> IfStmtComponent.getVariants(p, context)
                is PsiReadStmt -> ReadStmtComponent.getVariants(p, context)
                is PsiSkipStmt -> SkipStmtComponent.getVariants(p, context)
                is PsiWhileStmt -> WhileStmtComponent.getVariants(p, context)
                is PsiStmtList -> StmtListComponent.getVariants(p, context)
                is PsiWriteStmt -> WriteStmtComponent.getVariants(p, context)
                is PsiBinaryExpr -> BinaryExprComponent.getVariants(p, context)
                is PsiParenExpr -> ParenExprComponent.getVariants(p, context)
                is PsiBinaryBexpr -> BinaryBexprComponent.getVariants(p, context)
                is PsiParenBexpr -> ParenBexprComponent.getVariants(p, context)
                is PsiNotBexpr -> NotBexprComponent.getVariants(p, context)
                is PsiRelBexpr -> RelBexprComponent.getVariants(p, context)
                is PsiProcedure -> ProcedureComponent.getVariants(p, context)
                is PsiProcList -> ProcListComponent.getVariants(p, context)
                
                is WhileFile                    ->                    WhileFileComponent.getVariants(p, context)

                //Just cut from text
                else -> {
//                    println("AAA: ${p.getClass()}")
                    getEmptySet()
                }
            }

        return variants
    }

    public  fun areTemplatesFilled(): Boolean = areTemplatesFilled
    private var areTemplatesFilled  : Boolean = false

    public fun fillTemplateLists(templateFile: WhileFile) {
        areTemplatesFilled = true
        walker(templateFile, { p: PsiElement ->
            when (p) {
                is PsiAssignStmt -> AssignStmtComponent.getAndSaveTemplate(p)
                is PsiIfStmt -> IfStmtComponent.getAndSaveTemplate(p)
                is PsiReadStmt -> ReadStmtComponent.getAndSaveTemplate(p)
                is PsiSkipStmt -> SkipStmtComponent.getAndSaveTemplate(p)
                is PsiWhileStmt -> WhileStmtComponent.getAndSaveTemplate(p)
                is PsiStmtList -> StmtListComponent.getAndSaveTemplate(p)
                is PsiWriteStmt -> WriteStmtComponent.getAndSaveTemplate(p)
                is PsiBinaryExpr -> BinaryExprComponent.getAndSaveTemplate(p)
                is PsiParenExpr -> ParenExprComponent.getAndSaveTemplate(p)
                is PsiBinaryBexpr -> BinaryBexprComponent.getAndSaveTemplate(p)
                is PsiParenBexpr -> ParenBexprComponent.getAndSaveTemplate(p)
                is PsiNotBexpr -> NotBexprComponent.getAndSaveTemplate(p)
                is PsiRelBexpr -> RelBexprComponent.getAndSaveTemplate(p)
                is PsiProcedure -> ProcedureComponent.getAndSaveTemplate(p)
                is PsiProcList -> ProcListComponent.getAndSaveTemplate(p)
                
                else -> 5 + 5
            }
        })
    }

    private fun createElementFromText(p: PsiElement, text: String): PsiElement? {
        val factory = WhileElementFactory(getProject())
        if (factory == null) { return null }

        when (p) {
            is PsiAssignStmt -> return factory.createAssignStmtFromText(text)
            is PsiIfStmt -> return factory.createIfStmtFromText(text)
            is PsiReadStmt -> return factory.createReadStmtFromText(text)
            is PsiSkipStmt -> return factory.createSkipStmtFromText(text)
            is PsiWhileStmt -> return factory.createWhileStmtFromText(text)
            is PsiStmtList -> return factory.createStmtListFromText(text)
            is PsiWriteStmt -> return factory.createWriteStmtFromText(text)
            is PsiBinaryExpr -> return factory.createBinaryExprFromText(text)
            is PsiParenExpr -> return factory.createParenExprFromText(text)
            is PsiBinaryBexpr -> return factory.createBinaryBexprFromText(text)
            is PsiParenBexpr -> return factory.createParenBexprFromText(text)
            is PsiNotBexpr -> return factory.createNotBexprFromText(text)
            is PsiRelBexpr -> return factory.createRelBexprFromText(text)
            is PsiProcedure -> return factory.createProcedureFromText(text)
            is PsiProcList -> return factory.createProcListFromText(text)
            else -> return null
        }
    }

    private fun applyTmplt(p: PsiElement) {
        val formatSet = getVariants(p)

        val threadMXBean = ManagementFactory.getThreadMXBean()!!
        val startTime = threadMXBean.getCurrentThreadCpuTime()
        val chosenFormat = formatSet.head()
        if (chosenFormat == null) { return }

        fun replaceElement(newElement: PsiElement) {
            getProject().performUndoWrite { p.replace(newElement) }
        }

        val startLineOffset = p.getOffsetInStartLine()
        val newElementText = chosenFormat.toText(startLineOffset, "")

        if (p is WhileFile) {
            val document = PsiDocumentManager.getInstance(getProject())?.getDocument(p)
            val oldDocSize = document?.getText()?.size
            if (document == null || oldDocSize == null) { return }
            getProject().performUndoWrite {
                document.replaceString(0, oldDocSize, newElementText)
            }
            return
        }

        val statement: PsiElement
        try {
            val createdStatement = createElementFromText(p, newElementText)
            if (createdStatement == null) { return }
            statement = createdStatement
        } catch (e: IncorrectOperationException) { return }

        if (p is PsiCodeBlock && statement is PsiBlockStatement) {
            renewCache(p, statement)
            replaceElement(statement.getCodeBlock())
            return
        }
        renewCache(p, statement)
        replaceElement(statement)

        val endTime = threadMXBean.getCurrentThreadCpuTime()
        replaceTime += endTime - startTime
    }
}

