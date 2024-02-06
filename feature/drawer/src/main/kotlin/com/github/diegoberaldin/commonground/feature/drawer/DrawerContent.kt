package com.github.diegoberaldin.commonground.feature.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerSection
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.drawer.components.ImageListDrawerItem
import com.github.diegoberaldin.commonground.feature.drawer.components.StaticDrawerItem
import kotlinx.coroutines.launch
import com.github.diegoberaldin.commonground.core.commonui.R as commonR

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
) {
    val model : DrawerViewModel = injectViewModel<DefaultDrawerViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()
    val currentSection by drawerCoordinator.section.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
    ) {
        items(uiState.sources) { source ->
            ImageListDrawerItem(
                modifier = Modifier.fillMaxWidth(),
                sourceInfo = source,
                active = (currentSection as? DrawerSection.ImageList)?.source == source,
                onSelected = {
                    drawerCoordinator.changeSection(DrawerSection.ImageList(source))
                    coroutineScope.launch {
                        drawerCoordinator.send(DrawerEvent.Toggle)
                    }
                }
            )
        }
        item {
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = Spacing.s,
                    horizontal = Spacing.m,
                )
            )
        }
        item {
            StaticDrawerItem(
                title = stringResource(id = commonR.string.menu_item_favorites),
                active = currentSection == DrawerSection.Favorites,
                onSelected = {
                    drawerCoordinator.changeSection(DrawerSection.Favorites)
                    coroutineScope.launch {
                        drawerCoordinator.send(DrawerEvent.Toggle)
                    }
                },
            )
        }
        item {
            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = Spacing.s,
                    horizontal = Spacing.m,
                )
            )
        }
        item {
            StaticDrawerItem(
                title = stringResource(id = commonR.string.menu_item_settings),
                active = currentSection == DrawerSection.Settings,
                onSelected = {
                    drawerCoordinator.changeSection(DrawerSection.Settings)
                    coroutineScope.launch {
                        drawerCoordinator.send(DrawerEvent.Toggle)
                    }
                },
            )
        }
    }
}
