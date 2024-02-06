package com.github.diegoberaldin.commonground.feature.settings

import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.gallery.ResizeMode

interface SettingsViewModel :
    MviModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Event> {
    sealed interface Intent {
        data class ChangeTheme(val theme: UiTheme) : Intent
        data class ChangeResizeMode(val resizeMode: ResizeMode) : Intent
    }

    data class State(
        val theme: UiTheme = UiTheme.Dark,
        val resizeMode: ResizeMode? = null,
    )

    sealed interface Event
}
