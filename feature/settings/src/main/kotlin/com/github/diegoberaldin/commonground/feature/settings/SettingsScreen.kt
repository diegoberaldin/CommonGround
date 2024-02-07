package com.github.diegoberaldin.commonground.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.l10n.localized
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.settings.components.LanguageBottomSheet
import com.github.diegoberaldin.commonground.feature.settings.components.ResizeModeBottomSheet
import com.github.diegoberaldin.commonground.feature.settings.components.SelectThemeBottomSheet
import com.github.diegoberaldin.commonground.feature.settings.components.SettingsHeader
import com.github.diegoberaldin.commonground.feature.settings.components.SettingsRow
import kotlinx.coroutines.launch

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
    var languageBottomSheetOpen by remember { mutableStateOf(false) }
    var resizeModeBottomSheetOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
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
                        text = "menu_item_settings".localized(),
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
                    title = "settings_header_look_and_feel".localized(),
                )
                SettingsRow(
                    title = "settings_item_theme".localized(),
                    value = uiState.theme.toReadableString(),
                    onTap = {
                        themeBottomSheetOpen = true
                    },
                )
                SettingsRow(
                    title = "settings_item_language".localized(),
                    value = uiState.lang.toLanguageName(),
                    onTap = {
                        languageBottomSheetOpen = true
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.Settings,
                    title = "settings_header_behavior".localized(),
                )
                SettingsRow(
                    title = "settings_item_config_sources".localized(),
                    disclosureIndicator = true,
                    onTap = {
                        onConfigSources()
                    }
                )
                SettingsRow(
                    title = "settings_item_resize_mode".localized(),
                    value = uiState.resizeMode.toReadableString(),
                    onTap = {
                        resizeModeBottomSheetOpen = true
                    }
                )

                SettingsHeader(
                    icon = Icons.Default.BugReport,
                    title = "settings_header_debug".localized(),
                )
                SettingsRow(
                    title = "settings_item_version".localized(),
                    value = uiState.version,
                )
            }
        }
    }

    if (themeBottomSheetOpen) {
        SelectThemeBottomSheet(
            onDismiss = {
                themeBottomSheetOpen = false
            },
            onSelected = { value ->
                model.accept(SettingsViewModel.Intent.ChangeTheme(value))
            },
        )
    }

    if (languageBottomSheetOpen) {
        LanguageBottomSheet(
            onDismiss = {
                languageBottomSheetOpen = false
            },
            onSelected = { lang ->
                model.accept(SettingsViewModel.Intent.ChangeLanguage(lang))
            },
        )
    }

    if (resizeModeBottomSheetOpen) {
        ResizeModeBottomSheet(
            onDismiss = {
                resizeModeBottomSheetOpen = false
            },
            onSelected = { value ->
                model.accept(SettingsViewModel.Intent.ChangeResizeMode(value))
            },
        )
    }
}
