package com.intellij.whileLang

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

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