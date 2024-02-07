package com.github.diegoberaldin.commonground.feature.settings

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.utils.appversion.AppVersionRepository
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
    private val appVersionRepository: AppVersionRepository,
) : SettingsViewModel,
    DefaultMviModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Event>(
        initial = SettingsViewModel.State()
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            updateState {
                it.copy(version = appVersionRepository.getAppVersion())
            }
            themeRepository.theme.onEach { theme ->
                updateState { it.copy(theme = theme ?: UiTheme.Light) }
            }.launchIn(this)
            settingsRepository.current.onEach { settings ->
                updateState {
                    it.copy(
                        resizeMode = settings.resizeMode,
                        lang = settings.lang,
                    )
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: SettingsViewModel.Intent) {
        when (intent) {
            is SettingsViewModel.Intent.ChangeTheme -> updateTheme(intent.theme)
            is SettingsViewModel.Intent.ChangeResizeMode -> updateResizeMode(intent.resizeMode)
            is SettingsViewModel.Intent.ChangeLanguage -> updateLanguage(intent.lang)
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

    private fun updateLanguage(lang: String) {
        viewModelScope.launch {
            val settings = settingsRepository.current.value.copy(lang = lang)
            settingsRepository.update(settings)
        }
    }
}
