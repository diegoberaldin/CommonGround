package com.github.diegoberaldin.commonground.core.l10n.usecase

import com.github.diegoberaldin.commonground.core.l10n.data.LocalizableString
import java.io.InputStream

internal interface ParseXmlResourceUseCase {
    operator fun invoke(inputStream: InputStream): List<LocalizableString>
}
