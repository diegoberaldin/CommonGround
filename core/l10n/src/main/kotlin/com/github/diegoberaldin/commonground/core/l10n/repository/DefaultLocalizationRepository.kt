package com.github.diegoberaldin.commonground.core.l10n.repository

import com.github.diegoberaldin.commonground.core.l10n.localization.Localization
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
internal class DefaultLocalizationRepository(
    private val localization: Localization,
) : LocalizationRepository {

    override val currentLanguage = MutableStateFlow(localization.get("lang"))

    override fun changeLanguage(lang: String) {
        localization.setLanguage(lang)
    }

    override fun get(key: String): String = localization.get(key)

    override fun get(key: String, vararg args: Any): String {
        return localization.get(key).format(*args)
    }
}