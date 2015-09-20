package com.intellij.whileLang.printer;

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.util.IncorrectOperationException
import com.intellij.whileLang.WhileElementFactory
import com.intellij.whileLang.WhileFile
import com.intellij.whileLang.components.*
import com.intellij.whileLang.psi.impl.*
import org.jetbrains.format.FormatSet
import org.jetbrains.prettyPrinter.core.performUndoWrite
import org.jetbrains.prettyPrinter.core.printer.CommentConnectionUtils.VariantConstructionContext
import org.jetbrains.prettyPrinter.core.printer.Printer
import org.jetbrains.prettyPrinter.core.printer.PrinterSettings
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.templateBase.template.Template
import org.jetbrains.prettyPrinter.core.util.base.walker
import org.jetbrains.prettyPrinter.core.util.psiElement.getOffsetInStartLine
import java.lang.management.ManagementFactory


class WhilePrinter(
  templateFile: PsiFile?
, private val settings: PrinterSettings
): Printer(settings) {
    companion object {
        public fun create(templateFile: WhileFile, project: Project, width: Int): WhilePrinter =
                WhilePrinter(templateFile, PrinterSettings.createProjectSettings(width, project))
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

    override protected fun reprintElementWithChildren_AllMeaningful(psiElement: PsiElement) {
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

    override protected fun getTemplateVariants(p: PsiElement, context: VariantConstructionContext): FormatSet {
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

    override public fun fillTemplateLists(templateFile: PsiFile) {
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

    override public fun createElementFromText(p: PsiElement, text: String): PsiElement? {
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

    override protected fun applyTmplt(p: PsiElement) {
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

    override fun addTemplate(p: PsiElement) {
        areTemplatesFilled = true
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
    }

    override fun countTemplates(): Int {
        return AssignStmtComponent.getTemplates().size() +
                IfStmtComponent.getTemplates().size() +
                ReadStmtComponent.getTemplates().size() +
                SkipStmtComponent.getTemplates().size() +
                WhileStmtComponent.getTemplates().size() +
                StmtListComponent.getTemplates().size() +
                WriteStmtComponent.getTemplates().size() +
                BinaryExprComponent.getTemplates().size() +
                ParenExprComponent.getTemplates().size() +
                BinaryBexprComponent.getTemplates().size() +
                ParenBexprComponent.getTemplates().size() +
                NotBexprComponent.getTemplates().size() +
                RelBexprComponent.getTemplates().size() +
                ProcedureComponent.getTemplates().size() +
                ProcListComponent.getTemplates().size()

    }

    override fun getTemplate(p: PsiElement): Template<SmartInsertPlace>? {
        return when(p) {
            is PsiAssignStmt -> AssignStmtComponent.getTmplt(p)
            is PsiIfStmt -> IfStmtComponent.getTmplt(p)
            is PsiReadStmt -> ReadStmtComponent.getTmplt(p)
            is PsiSkipStmt -> SkipStmtComponent.getTmplt(p)
            is PsiWhileStmt -> WhileStmtComponent.getTmplt(p)
            is PsiStmtList -> StmtListComponent.getTmplt(p)
            is PsiWriteStmt -> WriteStmtComponent.getTmplt(p)
            is PsiBinaryExpr -> BinaryExprComponent.getTmplt(p)
            is PsiParenExpr -> ParenExprComponent.getTmplt(p)
            is PsiBinaryBexpr -> BinaryBexprComponent.getTmplt(p)
            is PsiParenBexpr -> ParenBexprComponent.getTmplt(p)
            is PsiNotBexpr -> NotBexprComponent.getTmplt(p)
            is PsiRelBexpr -> RelBexprComponent.getTmplt(p)
            is PsiProcedure -> ProcedureComponent.getTmplt(p)
            is PsiProcList -> ProcListComponent.getTmplt(p)

            else -> null
        }
    }
}

