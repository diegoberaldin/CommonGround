package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.cache.ImageModelCache
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultImageDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val imageModelCache: ImageModelCache,
) : ImageDetailViewModel,
    DefaultMviModel<ImageDetailViewModel.Intent, ImageDetailViewModel.State, ImageDetailViewModel.Event>(
        ImageDetailViewModel.State(),
    ) {

    override fun onCreate() {
        super.onCreate()
        val imageId: String? = savedStateHandle["id"]
        if (imageId != null) {
            load(imageId)
        }
    }

    override fun reduce(intent: ImageDetailViewModel.Intent) {
        when (intent) {
            is ImageDetailViewModel.Intent.Load -> {
                val imageId = intent.id
                if (imageId == savedStateHandle["id"]) {
                    return
                }
                load(imageId)
            }
        }
    }

    private fun load(id: String) {
        savedStateHandle["id"] = id
        viewModelScope.launch {
            imageModelCache.retrieve(id)?.also { image ->
                updateState {
                    it.copy(
                        title = image.title,
                        url = image.url,
                    )
                }
            }
        }
    }
}
