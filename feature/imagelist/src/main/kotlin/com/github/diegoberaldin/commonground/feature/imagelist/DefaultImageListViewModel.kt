package com.github.diegoberaldin.commonground.feature.imagelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.cache.ImageModelCache
import com.github.diegoberaldin.commonground.core.utils.imagepreload.ImagePreloadManager
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcherFactory
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
internal class DefaultImageListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val imageFetcherFactory: ImageFetcherFactory,
    private val imagePreloadManager: ImagePreloadManager,
    private val imageModelCache: ImageModelCache,
) : ImageListViewModel,
    DefaultMviModel<ImageListViewModel.Intent, ImageListViewModel.State, ImageListViewModel.Event>(
        ImageListViewModel.State(),
    ) {

    private var imageFetcher: ImageFetcher? =
        null

    override fun onCreate() {
        super.onCreate()
        val source: SourceInfoModel? = savedStateHandle["source"]
        if (source != null) {
            setSource(source)
        }
    }

    override fun reduce(intent: ImageListViewModel.Intent) {
        when (intent) {
            is ImageListViewModel.Intent.Load -> {
                val source = intent.source
                if (source != savedStateHandle["source"]) {
                    setSource(source)
                }
            }

            ImageListViewModel.Intent.Refresh -> viewModelScope.launch {
                refresh()
            }

            ImageListViewModel.Intent.FetchMore -> viewModelScope.launch {
                fetchMore()
            }

            is ImageListViewModel.Intent.OpenDetail -> {
                val image = intent.value
                val imageId = UUID.randomUUID().toString()
                viewModelScope.launch {
                    imageModelCache.store(id = imageId, value = image)
                    emit(ImageListViewModel.Event.OpenDetail(imageId))
                }
            }
        }
    }

    private fun setSource(source: SourceInfoModel) {
        savedStateHandle["source"] = source
        updateState {
            it.copy(
                title = source.name,
            )
        }
        // initialize image fetcher
        imageFetcher = imageFetcherFactory.create(source)
        viewModelScope.launch {
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
        val images = fetcher.getNextPage()
        updateState {
            val currentValues = it.images
            val valuesToAdd = images.filter {
                isRefreshing || currentValues.none { e -> e.url == it.url }
            }
            valuesToAdd.forEach { image ->
                imagePreloadManager.preload(image.url)
            }
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
}