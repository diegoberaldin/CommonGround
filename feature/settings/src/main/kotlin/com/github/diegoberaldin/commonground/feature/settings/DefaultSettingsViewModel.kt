package com.github.diegoberaldin.commonground.feature.settings

import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultSettingsViewModel : SettingsViewModel,
    DefaultMviModel<SettingsViewModel.Intent, SettingsViewModel.State, SettingsViewModel.Event>(
        initial = SettingsViewModel.State()
    ) {

    override fun reduce(intent: SettingsViewModel.Intent) {
        TODO("Not yet implemented")
    }
}
