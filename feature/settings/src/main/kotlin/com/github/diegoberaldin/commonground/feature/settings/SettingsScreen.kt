package com.github.diegoberaldin.commonground.feature.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.settings.components.SettingsHeader
import com.github.diegoberaldin.commonground.feature.settings.components.SettingsRow
import kotlinx.coroutines.launch
import com.github.diegoberaldin.commonground.core.commonui.R as commonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onConfigSources: () -> Unit,
) {
    val model: SettingsViewModel = injectViewModel<DefaultSettingsViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()
    val coroutineScope = rememberCoroutineScope()
    var themeBottomSheetOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
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
                        text = stringResource(commonR.string.menu_item_settings),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                SettingsHeader(
                    icon = Icons.Default.Style,
                    title = stringResource(commonR.string.settings_header_look_and_feel),
                )
                SettingsRow(
                    title = stringResource(commonR.string.settings_item_theme),
                    value = uiState.theme.toReadableString(),
                    onTap = {
                        themeBottomSheetOpen = true
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.Settings,
                    title = stringResource(commonR.string.settings_header_behavior),
                )
                SettingsRow(
                    title = stringResource(commonR.string.settings_item_config_sources),
                    disclosureIndicator = true,
                    onTap = {
                        onConfigSources()
                    }
                )
                SettingsRow(
                    title = stringResource(commonR.string.settings_item_resize_mode),
                )
            }
        }
    }

    if (themeBottomSheetOpen) {
        SelectThemeBottomSheet(onSelected = { value ->
            themeBottomSheetOpen = false
            if (value != null) {
                model.accept(SettingsViewModel.Intent.ChangeTheme(value))
            }
        })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SelectThemeBottomSheet(
    onSelected: (UiTheme?) -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        onDismissRequest = {
            onSelected(null)
        },
    ) {
        val values = listOf(
            UiTheme.Light,
            UiTheme.Dark,
            UiTheme.Black,
        )
        Column(
            modifier = Modifier.padding(horizontal = Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            for (value in values) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected(value)
                        }
                        .padding(vertical = Spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                ) {
                    Icon(
                        modifier = Modifier.size(IconSize.l),
                        imageVector = value.toIcon(),
                        contentDescription = null,
                    )
                    Text(
                        text = value.toReadableString(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.m))
        }
    }
}

@Composable
private fun UiTheme.toReadableString(): String = when (this) {
    UiTheme.Black -> stringResource(commonR.string.settings_theme_black)
    UiTheme.Dark -> stringResource(commonR.string.settings_theme_dark)
    UiTheme.Light -> stringResource(commonR.string.settings_theme_light)
}

@Composable
private fun UiTheme.toIcon(): ImageVector = when (this) {
    UiTheme.Black -> Icons.Default.DarkMode
    UiTheme.Dark -> Icons.Outlined.DarkMode
    UiTheme.Light -> Icons.Default.LightMode
}
