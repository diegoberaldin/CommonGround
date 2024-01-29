package com.github.diegoberaldin.commonground.home

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel

interface HomeViewModel : MviModel<HomeViewModel.Intent, HomeViewModel.State, HomeViewModel.Event> {
    sealed interface Intent {
        data class SetSource(val value: SourceInfoModel) : Intent
    }

    data class State(
        val currentSource: SourceInfoModel? = null,
    )

    sealed interface Event
}