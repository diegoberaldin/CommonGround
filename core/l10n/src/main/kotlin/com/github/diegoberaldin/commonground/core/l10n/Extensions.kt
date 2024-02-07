package com.github.diegoberaldin.commonground.core.l10n

import com.github.diegoberaldin.commonground.core.l10n.L10nHolder.l10n
import com.github.diegoberaldin.commonground.core.l10n.repository.LocalizationRepository

object L10nHolder {
    var l10n: LocalizationRepository? = null
}

fun String.localized(): String {
    return l10n?.get(this) ?: this
}

fun String.localized(vararg args: Any): String {
    return l10n?.get(key = this, args = args) ?: this
}
