package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.utils.share.ShareHelper
import com.github.diegoberaldin.commonground.domain.favorites.repository.FavoriteRepository
import com.github.diegoberaldin.commonground.domain.gallery.GalleryManager
import com.github.diegoberaldin.commonground.domain.gallery.ImageDownloadManager
import com.github.diegoberaldin.commonground.domain.gallery.WallpaperManager
import com.github.diegoberaldin.commonground.domain.gallery.WallpaperMode
import com.github.diegoberaldin.commonground.domain.imagefetch.cache.ImageModelCache
import com.github.diegoberaldin.commonground.domain.palette.PaletteCache
import com.github.diegoberaldin.commonground.domain.palette.PaletteGenerator
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultImageDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val imageModelCache: ImageModelCache,
    private val downloadManager: ImageDownloadManager,
    private val galleryManager: GalleryManager,
    private val wallpaperManager: WallpaperManager,
    private val favoriteRepository: FavoriteRepository,
    private val paletteGenerator: PaletteGenerator,
    private val paletteCache: PaletteCache,
    private val shareHelper: ShareHelper,
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

            ImageDetailViewModel.Intent.SaveToGallery -> saveToGallery()
            is ImageDetailViewModel.Intent.SetBackground -> setBackground(intent.mode)
            ImageDetailViewModel.Intent.ToggleFavorite -> toggleFavorite()
            ImageDetailViewModel.Intent.Share -> share()
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
                        favorite = image.favorite,
                    )
                }
                generatePalette()
            }
        }
    }

    private fun saveToGallery() {
        val url = uiState.value.url.takeIf { it.isNotEmpty() } ?: return
        viewModelScope.launch {
            runCatching {
                val bmp = downloadManager.getBitmap(url)
                if (bmp != null) {
                    galleryManager.saveToGallery(bmp)
                }
                emit(ImageDetailViewModel.Event.OperationSuccess)
            }.exceptionOrNull()?.message?.also {
                emit(ImageDetailViewModel.Event.OperationFailure(it))
            }
        }
    }

    private fun setBackground(mode: WallpaperMode) {
        val url = uiState.value.url.takeIf { it.isNotEmpty() } ?: return
        viewModelScope.launch {
            runCatching {
                val bmp = downloadManager.getBitmap(url)
                if (bmp != null) {
                    wallpaperManager.set(bmp, mode)
                }
                emit(ImageDetailViewModel.Event.OperationSuccess)
            }.exceptionOrNull()?.message?.also {
                emit(ImageDetailViewModel.Event.OperationFailure(it))
            }
        }
    }

    private fun toggleFavorite() {
        val id: String? = savedStateHandle["id"]
        val newValue = !uiState.value.favorite
        viewModelScope.launch {
            imageModelCache.retrieve(id.orEmpty())?.also { image ->
                if (newValue) {
                    favoriteRepository.add(image)
                } else {
                    favoriteRepository.remove(image.url)
                }
                updateState { it.copy(favorite = newValue) }
            }
        }
    }

    private suspend fun generatePalette() {
        val url = uiState.value.url.takeIf { it.isNotEmpty() } ?: return
        val palette = paletteCache.get(url) ?: run {
            val bitmap = downloadManager.getBitmap(url) ?: return@run null
            paletteGenerator.generatePalette(bitmap)
        }
        val colors = palette?.let {
            listOf(
                it.lightVibrantSwatch,
                it.lightMutedSwatch,
                it.vibrantSwatch,
                it.mutedSwatch,
                it.darkVibrantSwatch,
                it.darkMutedSwatch,
            ).mapNotNull { swatch ->
                swatch?.rgb?.let { rgb -> Color(rgb) }
            }
        }.orEmpty()
        updateState { it.copy(previewColors = colors) }
    }

    private fun share() {
        val url = uiState.value.url.takeIf { it.isNotEmpty() } ?: return
        shareHelper.share(url)
    }
}