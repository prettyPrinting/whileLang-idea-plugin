package com.intellij.whileLang

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.whileLang.parser.WhileParser
import com.intellij.whileLang.psi.WhileTypes
import java.awt.Color
import java.awt.Font
import java.io.Reader

/**
 * Created by me on 5/14/15.
 */
public class WhileLexerAdapter(): FlexAdapter(_WhileLexer(null)) {}

public class WhileParserDefinition(): ParserDefinition {
    companion object {
        public val WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
        public val COMMENTS    : TokenSet = TokenSet.create(WhileTypes.COMMENT)
        public val FILE: IFileElementType =
                IFileElementType(Language.findInstance<WhileLanguage>(javaClass<WhileLanguage>()))
    }

    override public fun createLexer(project: Project) = FlexAdapter(_WhileLexer(null))
    override public fun getWhitespaceTokens() = WHITE_SPACES
    override public fun getCommentTokens   () = COMMENTS
    override public fun getStringLiteralElements() = TokenSet.EMPTY
    override public fun createParser(project: Project) = WhileParser()
    override public fun getFileNodeType() = FILE
    override public fun createFile(viewProvider: FileViewProvider) = WhileFile(viewProvider)
    override public fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode) =
            ParserDefinition.SpaceRequirements.MAY
    override public fun createElement(node: ASTNode) = WhileTypes.Factory.createElement(node)
}

public class WhileSyntaxHighlighter(): SyntaxHighlighterBase() {
    companion object {
        private fun createTextAKey(s: String, t: TextAttributesKey) =
                TextAttributesKey.createTextAttributesKey(s, t)

        public val SEPARATOR: TextAttributesKey =
                createTextAKey("WHILE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        public val KEY      : TextAttributesKey =
                createTextAKey("WHILE_KEY", DefaultLanguageHighlighterColors.KEYWORD)
        public val VALUE    : TextAttributesKey =
                createTextAKey("WHILE_VALUE", DefaultLanguageHighlighterColors.STRING)
        public val COMMENT  : TextAttributesKey =
                createTextAKey("WHILE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        public val BAD_CHARACTER: TextAttributesKey =
                TextAttributesKey.createTextAttributesKey("WHILE_BAD_CHARACTER",
                        TextAttributes(Color.RED, null, null, null, Font.BOLD))

        private val BAD_CHAR_KEYS : Array<TextAttributesKey> = array(BAD_CHARACTER)
        private val SEPARATOR_KEYS: Array<TextAttributesKey> = array(SEPARATOR)
        private val KEY_KEYS      : Array<TextAttributesKey> = array(KEY)
        private val VALUE_KEYS    : Array<TextAttributesKey> = array(VALUE)
        private val COMMENT_KEYS  : Array<TextAttributesKey> = array(COMMENT)
        private val EMPTY_KEYS    : Array<TextAttributesKey> = array()
    }

    override public fun getHighlightingLexer() = FlexAdapter(_WhileLexer(null))
    override public fun getTokenHighlights(tokenType: IElementType) =
            when (tokenType) {
                WhileTypes.SEP    -> SEPARATOR_KEYS
                WhileTypes.ASSIGN -> SEPARATOR_KEYS
                WhileTypes.DO, WhileTypes.OD, WhileTypes.WHILE,
                WhileTypes.THEN, WhileTypes.ELSE, WhileTypes.IF, WhileTypes.FI,
                WhileTypes.READ, WhileTypes.WRITE, WhileTypes.SKIP -> KEY_KEYS
                WhileTypes.NUMBER, WhileTypes.TRUE,
                WhileTypes.FALSE        -> VALUE_KEYS
                WhileTypes.COMMENT      -> COMMENT_KEYS
                TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS
                else -> EMPTY_KEYS
            }
}

public class WhileSyntaxHighlighterFactory(): SyntaxHighlighterFactory() {
    override public fun getSyntaxHighlighter(project: Project, virtualFile: VirtualFile) =
            WhileSyntaxHighlighter()
}

public class WhileTokenType(debugName: String): IElementType(debugName, WhileLanguage.INSTANCE) {
    override public fun toString() = "WhileTokenType." + super.toString()
}
public class WhileElementType(debugName: String): IElementType(debugName, WhileLanguage.INSTANCE) {}