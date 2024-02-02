package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.cache.ImageModelCache
import com.github.diegoberaldin.commonground.domain.gallery.GalleryManager
import com.github.diegoberaldin.commonground.domain.gallery.ImageDownloadManager
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultImageDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val imageModelCache: ImageModelCache,
    private val downloadManager: ImageDownloadManager,
    private val galleryManager: GalleryManager,
) : ImageDetailViewModel,
    DefaultMviModel<ImageDetailViewModel.Intent, ImageDetailViewModel.State, ImageDetailViewModel.Event>(
        ImageDetailViewModel.State(),
    ) {

    override fun reduce(intent: ImageDetailViewModel.Intent) {
        when (intent) {
            is ImageDetailViewModel.Intent.Load -> {
                val imageId = intent.id
                load(imageId)
            }

            ImageDetailViewModel.Intent.SaveToGallery -> save()
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

    private fun save() {
        val url = uiState.value.url.takeIf { it.isNotEmpty() } ?: return
        viewModelScope.launch {
            runCatching {
                val bmp = downloadManager.getBitmap(url)
                if (bmp != null) {
                    galleryManager.saveToGallery(bmp)
                }
                emit(ImageDetailViewModel.Event.OperationSuccess)
            }
        }
    }
}
