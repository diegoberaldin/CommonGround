package com.github.diegoberaldin.commonground.core.l10n.localization

import android.content.Context
import com.github.diegoberaldin.commonground.core.l10n.data.LocalizableString
import com.github.diegoberaldin.commonground.core.l10n.localized
import com.github.diegoberaldin.commonground.core.l10n.usecase.ParseXmlResourceUseCase
import org.koin.core.annotation.Single
import java.io.InputStream

@Single
internal class DefaultLocalization(
    private val context: Context,
    private val parseResource: ParseXmlResourceUseCase,
) : Localization {

    private val defaultValues: List<LocalizableString> by lazy {
        getInputStream("en").use {
            load(it)
        }
    }

    private var localizables: List<LocalizableString> = emptyList()

    override fun setLanguage(lang: String) {
        localizables = getInputStream(lang).use {
            load(it)
        }
    }

    override fun getLanguage(): String = "lang".localized()

    override fun get(key: String) = localizables.firstOrNull { it.key == key }?.value
        ?: defaultValues.firstOrNull { it.key == key }?.value
        ?: key

    private fun getInputStream(lang: String): InputStream {
        val path = "l10n/${lang}/strings.xml"
        return context.assets.open(path)
    }

    private fun load(inputStream: InputStream): List<LocalizableString> = parseResource(inputStream)
}
