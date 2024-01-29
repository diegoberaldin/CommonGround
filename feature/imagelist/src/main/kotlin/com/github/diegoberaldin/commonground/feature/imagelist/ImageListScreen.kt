package com.github.diegoberaldin.commonground.feature.imagelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.diegoberaldin.commonground.core.appearance.theme.CornerSize
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageListScreen(
    modifier: Modifier = Modifier,
    source: SourceInfoModel? = null,
    toggleDrawer: (() -> Unit)? = null,
    onOpenDetail: ((String) -> Unit)? = null,
) {
    val model: ImageListViewModel = injectViewModel<DefaultImageListViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    LaunchedEffect(source) {
        if (source != null) {
            model.accept(ImageListViewModel.Intent.Load(source))
        }
    }
    LaunchedEffect(model) {
        model.events.onEach { event ->
            when (event) {
                is ImageListViewModel.Event.OpenDetail -> onOpenDetail?.invoke(event.id)
            }
        }.launchIn(this)
    }

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            model.accept(ImageListViewModel.Intent.Refresh)
            pullRefreshState.endRefresh()
        }
    }

    Scaffold(
        modifier = modifier
            .padding(horizontal = Spacing.s),
        topBar = {
            TopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            toggleDrawer?.invoke()
                        },
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                    )
                },
                title = {
                    Text(text = uiState.title)
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.m, vertical = Spacing.xs)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            if (uiState.initial) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                if (!uiState.initial && uiState.images.isEmpty() && !uiState.loading) {
                    item {
                        Text("No item to display")
                    }
                }
                items(
                    count = uiState.images.size,
                    key = { uiState.images[it].url },
                ) {
                    val image = uiState.images[it]
                    ImageCard(
                        modifier = Modifier.clickable {
                            model.accept(ImageListViewModel.Intent.OpenDetail(image))
                        },
                        image = image,
                    )
                }
                item {
                    if (!uiState.initial && !uiState.loading && uiState.canFetchMore) {
                        LaunchedEffect(true) {
                            model.accept(ImageListViewModel.Intent.FetchMore)
                        }
                    }
                }
                if (uiState.loading && !uiState.refreshing) {
                    item(
                        span = {
                            GridItemSpan(this.maxLineSpan)
                        },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.s),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(IconSize.m),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )
        }
    }
}

@Composable
internal fun ImageCard(
    image: ImageModel,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(CornerSize.m)
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondary, shape = shape)
            .clip(shape)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f),
            model = image.url,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
    }
}