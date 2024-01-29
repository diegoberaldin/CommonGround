package com.github.diegoberaldin.commonground.feature.drawer

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel

interface DrawerViewModel :
    MviModel<DrawerViewModel.Intent, DrawerViewModel.State, DrawerViewModel.Event> {
    sealed interface Intent
    data class State(
        val sources: List<SourceInfoModel> = emptyList(),
    )

    sealed interface Event
}

