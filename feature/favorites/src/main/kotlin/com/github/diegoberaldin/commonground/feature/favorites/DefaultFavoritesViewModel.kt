package com.github.diegoberaldin.commonground.feature.favorites

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.utils.imagepreload.ImagePreloadManager
import com.github.diegoberaldin.commonground.domain.favorites.repository.FavoriteRepository
import com.github.diegoberaldin.commonground.domain.imagefetch.cache.ImageModelCache
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
internal class DefaultFavoritesViewModel(
    private val imagePreloadManager: ImagePreloadManager,
    private val imageModelCache: ImageModelCache,
    private val favoriteRepository: FavoriteRepository,
) : FavoritesViewModel,
    DefaultMviModel<FavoritesViewModel.Intent, FavoritesViewModel.State, FavoritesViewModel.Event>(
        FavoritesViewModel.State(),
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            favoriteRepository.getAll().onEach { images ->
                images.onEach {
                    imagePreloadManager.preload(it.url)
                }

                updateState {
                    it.copy(
                        images = images,
                        initial = false,
                    )
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: FavoritesViewModel.Intent) {
        when (intent) {
            is FavoritesViewModel.Intent.OpenDetail -> {
                val image = intent.image
                val imageId = UUID.randomUUID().toString()
                viewModelScope.launch {
                    imageModelCache.store(id = imageId, value = image)
                    emit(FavoritesViewModel.Event.OpenDetail(imageId))
                }
            }

            is FavoritesViewModel.Intent.ToggleFavorite -> toggleFavorite(intent.image)
        }
    }

    private fun toggleFavorite(image: ImageModel) {
        viewModelScope.launch {
            favoriteRepository.remove(image.url)
        }
    }
}