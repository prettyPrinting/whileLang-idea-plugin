package com.intellij.whileLang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.WhileFileType
import com.intellij.whileLang.WhileLanguage

/**
 * User: anlun
 */

public class WhileTokenType(debugName: String): IElementType(debugName, WhileLanguage.INSTANCE) {
    override public fun toString() = "WhileTokenType." + super.toString()
}
public class WhileElementType(debugName: String): IElementType(debugName, WhileLanguage.INSTANCE) {}

public class WhileFile(viewProvider: FileViewProvider): PsiFileBase(viewProvider, WhileLanguage.INSTANCE) {
    override public fun getFileType() = WhileFileType.INSTANCE
    override public fun toString() = "While File"
    override public fun getIcon(flags: Int) = super.getIcon(flags)
}