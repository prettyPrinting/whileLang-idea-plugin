package com.intellij.whileLang

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
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.psi.WhileTypes
import java.awt.*

/**
 * User: anlun
 */
public class WhileLanguage(): Language("While") {
    companion object { val INSTANCE = WhileLanguage() }
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

    override public fun getHighlightingLexer() = FlexAdapter(WhileLexer(null))
    override public fun getTokenHighlights(tokenType: IElementType) =
            when (tokenType) {
                WhileTypes.SEPARATOR    -> SEPARATOR_KEYS
                WhileTypes.ASSIGN       -> SEPARATOR_KEYS
                WhileTypes.DO, WhileTypes.OD, WhileTypes.WHILE,
                WhileTypes.THEN, WhileTypes.ELSE, WhileTypes.IF, WhileTypes.FI,
                WhileTypes.READ, WhileTypes.WRITE -> KEY_KEYS
                WhileTypes.NUMBER       -> VALUE_KEYS
                WhileTypes.COMMENT      -> COMMENT_KEYS
                TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS
                else -> EMPTY_KEYS
            }
}

public class WhileSyntaxHighlighterFactory(): SyntaxHighlighterFactory() {
    override public fun getSyntaxHighlighter(project: Project, virtualFile: VirtualFile) =
            WhileSyntaxHighlighter()
}
