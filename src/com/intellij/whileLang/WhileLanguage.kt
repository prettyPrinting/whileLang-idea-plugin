package com.intellij.whileLang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.psi.WhileTypes
import com.intellij.whileLang.psi.impl.*
import java.awt.*
import com.intellij.whileLang.*

public class WhileLanguage(): Language("While") {
    companion object { val INSTANCE = WhileLanguage() }
}

public class WhileFile(provider: FileViewProvider) : PsiFileBase(provider, WhileLanguage.INSTANCE) {
    override fun getFileType(): FileType { return WhileFileType.INSTANCE }
    override fun accept(visitor: PsiElementVisitor) { visitor.visitFile(this) }
    public fun getStmtList() : PsiStmtList? = findChildByClass(javaClass<PsiStmtList>())
    public fun getProcList() : PsiProcList? = findChildByClass(javaClass<PsiProcList>())
    override public fun toString() = "While File"
    override public fun getIcon(flags: Int) = super.getIcon(flags)
}

public class WhileIcons() {
    companion object {
        val FILE : Icon = IconLoader.getIcon("/com/intellij/icons/gear.png")
    }
}

public class WhileFileType(): LanguageFileType(WhileLanguage.INSTANCE) {
    companion object { val INSTANCE = WhileFileType() }

    override public fun getName            () = "While file"
    override public fun getDescription     () = "While language file"
    override public fun getDefaultExtension() = "l"
    override public fun getIcon            () = WhileIcons.FILE
}

public class WhileFileTypeFactory(): FileTypeFactory() {
    override public fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(WhileFileType.INSTANCE)
    }
}

public class WhileElementFactory(val project: Project) {
    public fun createFileFromText(text: String): com.intellij.whileLang.WhileFile {
        return PsiFileFactory.getInstance(project).createFileFromText("tmp.l", WhileLanguage.INSTANCE, text) as com.intellij.whileLang.WhileFile
    }
    public fun createProcedureFromText(text: String): PsiProcedure? {
        return createFileFromText(text).getProcList()?.getProcedureList()?.get(0)
    }

    public fun createStmtFromText(text: String): PsiStmt? {
       return createFileFromText(text).getStmtList()?.getStmtList()?.get(0)
    }
    public fun createWriteStmtFromText(text: String): PsiWriteStmt? = createStmtFromText(text) as? PsiWriteStmt
    public fun createReadStmtFromText(text: String): PsiReadStmt? = createStmtFromText(text) as? PsiReadStmt
    public fun createAssignStmtFromText(text: String): PsiAssignStmt?  = createStmtFromText(text) as? PsiAssignStmt
    public fun createIfStmtFromText(text: String): PsiIfStmt?  = createStmtFromText(text) as? PsiIfStmt
    public fun createWhileStmtFromText(text: String): PsiWhileStmt?  = createStmtFromText(text) as? PsiWhileStmt
    public fun createSkipStmtFromText(text: String): PsiSkipStmt?  = createStmtFromText(text) as? PsiSkipStmt

    public fun createStmtListFromText(text: String): PsiStmtList?  {
        return createFileFromText(text).getStmtList()
    }
    public fun createProcListFromText(text: String): PsiProcList? {
        return createFileFromText(text).getProcList()
    }

    public fun createExprFromText(text: String): PsiExpr? {
        val writeStmt = createFileFromText("write($text);").getStmtList()?.getStmtList()?.get(0) as? PsiWriteStmt
        return writeStmt?.getExpr()
    }
    public fun createParenExprFromText(text: String): PsiParenExpr? = createExprFromText(text) as? PsiParenExpr
    public fun createBinaryExprFromText(text: String): PsiBinaryExpr? = createExprFromText(text) as? PsiBinaryExpr

    public fun createBexprFromText(text: String): PsiBexpr? {
        val whileStmt = createFileFromText("while ($text) do skip; od").getStmtList()?.getStmtList()?.get(0) as? PsiWhileStmt
        return whileStmt?.getBexpr()
    }
    public fun createParenBexprFromText(text: String): PsiParenBexpr? = createBexprFromText(text) as? PsiParenBexpr
    public fun createNotBexprFromText(text: String): PsiNotBexpr? = createBexprFromText(text) as? PsiNotBexpr
    public fun createBinaryBexprFromText(text: String): PsiBinaryBexpr? = createBexprFromText(text) as? PsiBinaryBexpr
    public fun createRelBexprFromText(text: String): PsiRelBexpr? = createBexprFromText(text) as? PsiRelBexpr
}