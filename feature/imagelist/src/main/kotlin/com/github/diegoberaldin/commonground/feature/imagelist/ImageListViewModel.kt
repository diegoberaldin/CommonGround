package com.github.diegoberaldin.commonground.feature.imagelist

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

interface ImageListViewModel :
    MviModel<ImageListViewModel.Intent, ImageListViewModel.State, ImageListViewModel.Event> {
    sealed interface Intent {
        data object Refresh : Intent
        data object FetchMore : Intent
        data class OpenDetail(val value: ImageModel) : Intent
    }

    data class State(
        val title: String = "",
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = false,
        val images: List<ImageModel> = emptyList()
    )

    sealed interface Event {

        data object BackToTop : Event
        data class OpenDetail(val id: String) : Event
    }
}
