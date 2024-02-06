package com.github.diegoberaldin.commonground.feature.settings.configsources

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

interface ConfigSourcesViewModel :
    MviModel<ConfigSourcesViewModel.Intent, ConfigSourcesViewModel.State, ConfigSourcesViewModel.Event> {
    sealed interface Intent {
        data class DeleteItem(val source: SourceInfoModel) : Intent
        data object ResetAll : Intent
    }

    data class State(
        val sources: List<SourceInfoModel> = emptyList(),
    )

    sealed interface Event
}
