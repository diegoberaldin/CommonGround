package com.github.diegoberaldin.commonground.feature.settings

import android.provider.MediaStore.Images
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
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
}