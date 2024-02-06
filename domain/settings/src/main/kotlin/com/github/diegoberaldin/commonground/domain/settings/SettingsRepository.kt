package com.github.diegoberaldin.commonground.domain.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val current: StateFlow<SettingsModel>

    suspend fun update(value: SettingsModel)
}
