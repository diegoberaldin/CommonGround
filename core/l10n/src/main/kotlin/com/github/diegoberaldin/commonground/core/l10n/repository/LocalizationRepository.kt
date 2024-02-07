package com.github.diegoberaldin.commonground.core.l10n.repository

import kotlinx.coroutines.flow.StateFlow

interface LocalizationRepository {

    val currentLanguage: StateFlow<String>

    fun changeLanguage(lang: String)

    fun get(key: String): String

    fun get(key: String, vararg args: Any): String
}
