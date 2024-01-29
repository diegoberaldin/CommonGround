package com.github.diegoberaldin.commonground.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.feature.drawer.DrawerContent
import com.github.diegoberaldin.commonground.feature.imagelist.ImageListScreen
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
    onOpenDetail: ((String) -> Unit)? = null,
) {
    val model = injectViewModel<DefaultHomeViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun toggleDrawer() {
        drawerState.apply {
            scope.launch {
                if (isClosed) {
                    open()
                } else {
                    close()
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    currentSourceInfo = uiState.currentSource,
                    modifier = Modifier.padding(
                        vertical = Spacing.m,
                    ),
                    onItemSelected = {
                        toggleDrawer()
                        model.accept(HomeViewModel.Intent.SetSource(it))
                    },
                )
            }
        },
        content = {
            ImageListScreen(
                modifier = Modifier.fillMaxSize(),
                source = uiState.currentSource,
                toggleDrawer = {
                    toggleDrawer()
                },
                onOpenDetail = onOpenDetail,
            )
        },
    )
}