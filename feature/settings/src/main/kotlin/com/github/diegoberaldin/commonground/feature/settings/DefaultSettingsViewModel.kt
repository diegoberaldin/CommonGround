package com.github.diegoberaldin.commonground.feature.settings

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.gallery.ResizeMode
import com.github.diegoberaldin.commonground.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultSettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
) : SettingsViewModel,
    DefaultMviModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Event>(
        initial = SettingsViewModel.State()
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            themeRepository.theme.onEach { theme ->
                updateState { it.copy(theme = theme ?: UiTheme.Light) }
            }.launchIn(this)
            settingsRepository.current.onEach { settings ->
                updateState { it.copy(resizeMode = settings.resizeMode) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: SettingsViewModel.Intent) {
        when (intent) {
            is SettingsViewModel.Intent.ChangeTheme -> updateTheme(intent.theme)
            is SettingsViewModel.Intent.ChangeResizeMode -> updateResizeMode(intent.resizeMode)
        }
    }

    private fun updateTheme(theme: UiTheme) {
        viewModelScope.launch {
            themeRepository.changeTheme(theme)
            val settings = settingsRepository.current.value.copy(theme = theme)
            settingsRepository.update(settings)
        }
    }

    private fun updateResizeMode(resizeMode: ResizeMode?) {
        viewModelScope.launch {
            val settings = settingsRepository.current.value.copy(resizeMode = resizeMode)
            settingsRepository.update(settings)
        }
    }
}
