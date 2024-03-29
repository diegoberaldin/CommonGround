package com.github.diegoberaldin.commonground.feature.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.l10n.localized
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.favorites.components.FavoriteCard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onOpenDetail: ((String) -> Unit)? = null,
) {
    val model: FavoritesViewModel = injectViewModel<DefaultFavoritesViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()
    val coroutineScope = rememberCoroutineScope()
    var topAppBarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(model) {
        model.events.onEach { event ->
            when (event) {
                is FavoritesViewModel.Event.OpenDetail -> onOpenDetail?.invoke(event.imageId)
            }
        }.launchIn(this)
    }

    Scaffold(
        topBar = {
            if (topAppBarVisible) {
                TopAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(Spacing.xxs)
                                .clickable {
                                    coroutineScope.launch {
                                        drawerCoordinator.send(DrawerEvent.Toggle)
                                    }
                                },
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                        )
                    },
                    title = {
                        Text(
                            modifier = Modifier.padding(start = Spacing.xs),
                            text = "menu_item_favorites".localized(),
                        )
                    },
                )
            }
        },
    ) { padding ->
        LaunchedEffect(Unit) {
            // avoid flickering
            topAppBarVisible = true
        }
        Box(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = Spacing.xs)
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = 160.dp),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                if (!uiState.initial && uiState.images.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.m),
                            text = "message_empty_list".localized()
                        )
                    }
                }
                items(
                    count = uiState.images.size,
                    key = { uiState.images[it].url },
                ) {
                    val image = uiState.images[it]
                    FavoriteCard(
                        modifier = Modifier.clickable {
                            model.accept(FavoritesViewModel.Intent.OpenDetail(image))
                        },
                        image = image,
                        onFavoriteTap = {
                            model.accept(FavoritesViewModel.Intent.ToggleFavorite(image))
                        },
                    )
                }
            }
        }
    }
}
