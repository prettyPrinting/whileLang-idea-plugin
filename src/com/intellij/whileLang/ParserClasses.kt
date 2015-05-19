package com.intellij.whileLang

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.whileLang.parser.WhileParser
import com.intellij.whileLang.psi.WhileFile
import com.intellij.whileLang.psi.WhileTypes
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