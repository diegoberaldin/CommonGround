package com.github.diegoberaldin.commonground.feature.imagedetail

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.gallery.WallpaperMode

interface ImageDetailViewModel :
    MviModel<ImageDetailViewModel.Intent, ImageDetailViewModel.State, ImageDetailViewModel.Event> {
    sealed interface Intent {
        data class Load(val id: String) : Intent

        data object SaveToGallery : Intent

        data class SetBackground(val mode: WallpaperMode) : Intent
        data object ToggleFavorite : Intent
    }

    data class State(
        val title: String = "",
        val url: String = "",
        val favorite: Boolean = false,
    )

    sealed interface Event {
        data object OperationSuccess : Event
        data class OperationFailure(val message: String) : Event
    }
}
