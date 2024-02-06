package com.github.diegoberaldin.commonground.core.l10n.localization

interface Localization {
    fun get(key: String): String

    fun setLanguage(lang: String)

    fun getLanguage(): String
}
