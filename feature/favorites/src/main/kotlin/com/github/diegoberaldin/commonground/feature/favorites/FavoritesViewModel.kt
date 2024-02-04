package com.github.diegoberaldin.commonground.feature.favorites

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

interface FavoritesViewModel :
    MviModel<FavoritesViewModel.Intent, FavoritesViewModel.State, FavoritesViewModel.Event> {
    sealed interface Intent {
        data class OpenDetail(val image: ImageModel) : Intent
        data class ToggleFavorite(val image: ImageModel) : Intent
    }

    data class State(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val images: List<ImageModel> = emptyList()
    )

    sealed interface Event {
        data class OpenDetail(val imageId: String) : Event
    }
}
