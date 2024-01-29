package com.github.diegoberaldin.commonground.feature.imagedetail

import com.github.diegoberaldin.commonground.core.architecture.MviModel

interface ImageDetailViewModel :
    MviModel<ImageDetailViewModel.Intent, ImageDetailViewModel.State, ImageDetailViewModel.Event> {
    sealed interface Intent {
        data class Load(val id: String) : Intent
    }

    data class State(
        val title: String = "",
        val url: String = "",
    )

    sealed interface Event
}
