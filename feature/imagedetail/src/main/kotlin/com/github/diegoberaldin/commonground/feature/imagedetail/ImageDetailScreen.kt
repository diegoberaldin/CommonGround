package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberSmartRecord
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.domain.gallery.WallpaperMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.diegoberaldin.commonground.core.commonui.R as commonR

@Composable
fun ImageDetailScreen(
    id: String,
) {
    val model: ImageDetailViewModel = injectViewModel<DefaultImageDetailViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var wallpaperModalSelectionOpen by remember { mutableStateOf(false) }
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()

    DisposableEffect(Unit) {
        drawerCoordinator.changeGesturesEnabled(false)
        onDispose {
            drawerCoordinator.changeGesturesEnabled(true)
        }
    }
    LaunchedEffect(id) {
        model.accept(ImageDetailViewModel.Intent.Load(id))
    }
    val successMessage = stringResource(id = commonR.string.message_operation_success)
    LaunchedEffect(model) {
        model.events.onEach { event ->
            when (event) {
                ImageDetailViewModel.Event.OperationSuccess -> {
                    snackbarHostState.showSnackbar(successMessage)
                }

                is ImageDetailViewModel.Event.OperationFailure -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }.launchIn(this)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = uiState.url,
            contentDescription = uiState.title,
            contentScale = ContentScale.FillBounds,
        )

        OverlayBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            title = uiState.title,
            favorite = uiState.favorite,
            previewColors = uiState.previewColors,
            onSave = {
                model.accept(ImageDetailViewModel.Intent.SaveToGallery)
            },
            onSet = {
                wallpaperModalSelectionOpen = true
            },
            onToggleFavorite = {
                model.accept(ImageDetailViewModel.Intent.ToggleFavorite)
            },
            onShare = {
                model.accept(ImageDetailViewModel.Intent.Share)
            },
        )

        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing.m),
            hostState = snackbarHostState
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }

    if (wallpaperModalSelectionOpen) {
        SelectWallpaperModeModal(
            onSelect = { mode ->
                wallpaperModalSelectionOpen = false
                if (mode != null) {
                    model.accept(ImageDetailViewModel.Intent.SetBackground(mode))
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SelectWallpaperModeModal(
    onSelect: (WallpaperMode?) -> Unit,
) {
    val values = listOf(
        WallpaperMode.HomeScreen,
        WallpaperMode.LockScreen,
        WallpaperMode.Both,
    )
    ModalBottomSheet(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        onDismissRequest = {
            onSelect(null)
        },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            for (value in values) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(value)
                        }
                        .padding(vertical = Spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                ) {
                    Icon(
                        modifier = Modifier.size(IconSize.l),
                        imageVector = value.icon,
                        contentDescription = null,
                    )
                    Text(
                        text = value.title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.m))
        }
    }
}

private val WallpaperMode.title: String
    @Composable
    get() = when (this) {
        WallpaperMode.Both -> stringResource(id = commonR.string.wallpaper_mode_both)
        WallpaperMode.HomeScreen -> stringResource(id = commonR.string.wallpaper_mode_home_screen)
        WallpaperMode.LockScreen -> stringResource(id = commonR.string.wallpaper_mode_lock_screen)
    }

private val WallpaperMode.icon: ImageVector
    get() = when (this) {
        WallpaperMode.Both -> Icons.Default.FiberSmartRecord
        WallpaperMode.HomeScreen -> Icons.Default.Smartphone
        WallpaperMode.LockScreen -> Icons.Default.ScreenLockPortrait
    }
