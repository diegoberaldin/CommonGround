package com.github.diegoberaldin.commonground.feature.imagelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerSection
import com.github.diegoberaldin.commonground.core.utils.imagepreload.ImagePreloadManager
import com.github.diegoberaldin.commonground.domain.favorites.repository.FavoriteRepository
import com.github.diegoberaldin.commonground.domain.imagefetch.cache.ImageModelCache
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcherFactory
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
internal class DefaultImageListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val imageFetcherFactory: ImageFetcherFactory,
    private val imagePreloadManager: ImagePreloadManager,
    private val imageModelCache: ImageModelCache,
    private val drawerCoordinator: DrawerCoordinator,
    private val favoriteRepository: FavoriteRepository,
) : ImageListViewModel,
    DefaultMviModel<ImageListViewModel.Intent, ImageListViewModel.State, ImageListViewModel.Event>(
        ImageListViewModel.State(),
    ) {

    private var imageFetcher: ImageFetcher? = null

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            drawerCoordinator.section
                .filterIsInstance<DrawerSection.ImageList>()
                .map { it.source }
                .onEach { source: SourceInfoModel ->
                    setSource(source)
                }.launchIn(this)

            favoriteRepository.getAll().onEach { favorites ->
                updateState {
                    it.copy(
                        images = it.images.map { image ->
                            image.copy(
                                favorite = favorites.any { f -> f.url == image.url }
                            )
                        },
                    )
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: ImageListViewModel.Intent) {
        when (intent) {
            ImageListViewModel.Intent.Refresh -> viewModelScope.launch {
                refresh()
            }

            ImageListViewModel.Intent.FetchMore -> viewModelScope.launch {
                fetchMore()
            }

            is ImageListViewModel.Intent.OpenDetail -> {
                val image = intent.image
                val imageId = UUID.randomUUID().toString()
                viewModelScope.launch {
                    imageModelCache.store(id = imageId, value = image)
                    emit(ImageListViewModel.Event.OpenDetail(imageId))
                }
            }

            is ImageListViewModel.Intent.ToggleFavorite -> toggleFavorite(intent.image)
        }
    }

    private fun setSource(source: SourceInfoModel) {
        if (source == savedStateHandle["source"]) {
            return
        }
        updateState { it.copy(title = source.name) }
        savedStateHandle["source"] = source
        // initialize image fetcher
        imageFetcher = imageFetcherFactory.create(source)
        viewModelScope.launch {
            emit(ImageListViewModel.Event.BackToTop)
            updateState {
                it.copy(
                    initial = true,
                    images = emptyList(),
                )
            }
            refresh()
        }
    }

    private suspend fun refresh() {
        val fetcher = imageFetcher ?: return
        updateState { it.copy(refreshing = true) }
        fetcher.reset()
        fetchMore()
    }

    private suspend fun fetchMore() {
        val fetcher = imageFetcher ?: return
        if (!fetcher.canFetchMore) {
            updateState { it.copy(loading = false, refreshing = false) }
            return
        }

        updateState { it.copy(loading = true) }
        val isRefreshing = uiState.value.refreshing
        val currentValues = uiState.value.images
        val images = fetcher.getNextPage()
        val valuesToAdd = images
            .filter {
                isRefreshing || currentValues.none { e -> e.url == it.url }
            }
            .distinctBy { e -> e.url }
            .map { image ->
                imagePreloadManager.preload(image.url)
                val isFavorite = favoriteRepository.isFavorite(image.url)
                image.copy(
                    favorite = isFavorite,
                )
            }

        updateState {
            it.copy(
                images = if (isRefreshing) {
                    valuesToAdd
                } else {
                    currentValues + valuesToAdd
                },
                canFetchMore = fetcher.canFetchMore,
                initial = false,
                loading = false,
                refreshing = false,
            )
        }
    }

    private fun toggleFavorite(image: ImageModel) {
        val newValue = !image.favorite
        viewModelScope.launch {
            if (newValue) {
                favoriteRepository.add(image)
            } else {
                favoriteRepository.remove(image.url)
            }

            updateState {
                it.copy(
                    images = it.images.map { e ->
                        if (e.url == image.url) {
                            e.copy(favorite = newValue)
                        } else {
                            e
                        }
                    },
                )
            }
        }
    }
}