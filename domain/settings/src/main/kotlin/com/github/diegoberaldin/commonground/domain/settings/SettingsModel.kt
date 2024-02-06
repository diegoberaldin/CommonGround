package com.github.diegoberaldin.commonground.domain.settings

import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.domain.gallery.ResizeMode

data class SettingsModel(
    val lang: String? = null,
    val theme: UiTheme? = null,
    val resizeMode: ResizeMode? = null,
)