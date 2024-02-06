package com.github.diegoberaldin.commonground.feature.settings

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultSettingsViewModel(
    private val themeRepository: ThemeRepository,
) : SettingsViewModel,
    DefaultMviModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Event>(
        initial = SettingsViewModel.State()
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            themeRepository.theme.onEach { theme ->
                updateState { it.copy(theme = theme) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: SettingsViewModel.Intent) {
        when (intent) {
            is SettingsViewModel.Intent.ChangeTheme -> {
                themeRepository.changeTheme(intent.theme)
            }
        }
    }
}
