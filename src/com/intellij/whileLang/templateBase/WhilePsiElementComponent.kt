package com.intellij.whileLang.templateBase

import com.intellij.psi.PsiElement
import com.intellij.whileLang.printer.WhilePrinter
import org.jetbrains.prettyPrinter.core.templateBase.PsiElementComponent
import org.jetbrains.prettyPrinter.core.templateBase.template.SmartInsertPlace
import org.jetbrains.prettyPrinter.core.templateBase.template.Template

abstract class WhilePsiElementComponent<ET: PsiElement , IPT: SmartInsertPlace , T: Template<IPT>>(
    override final val printer: WhilePrinter
): PsiElementComponent<ET, IPT, T>(printer){}
