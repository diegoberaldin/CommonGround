package com.github.diegoberaldin.commonground.feature.settings.configsources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.Option
import com.github.diegoberaldin.commonground.core.commonui.OptionId
import com.github.diegoberaldin.commonground.core.commonui.R
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.core.utils.toLocalDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigSourcesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val model: ConfigSourcesViewModel = injectViewModel<DefaultConfigSourcesViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()

    DisposableEffect(Unit) {
        drawerCoordinator.changeGesturesEnabled(false)
        onDispose {
            drawerCoordinator.changeGesturesEnabled(true)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onBack()
                        },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.settings_item_config_sources),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = Spacing.xs),
                    ) {
                        val options = listOf(
                            Option(
                                id = OptionId.Add,
                                text = stringResource(R.string.action_add),
                            ),
                            Option(
                                id = OptionId.Reset,
                                text = stringResource(R.string.action_reset),
                            ),
                        )
                        var optionsExpanded by remember { mutableStateOf(false) }
                        var optionsOffset by remember { mutableStateOf(Offset.Zero) }
                        Icon(
                            modifier = Modifier
                                .size(IconSize.l)
                                .padding(Spacing.xs)
                                .padding(top = Spacing.xxs)
                                .onGloballyPositioned {
                                    optionsOffset = it.positionInParent()
                                }
                                .clickable {
                                    optionsExpanded = true
                                },
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                        if (optionsExpanded) {
                            DropdownMenu(
                                modifier = Modifier,
                                offset = DpOffset(
                                    x = optionsOffset.x.toLocalDp(),
                                    y = optionsOffset.y.toLocalDp(),
                                ),
                                expanded = optionsExpanded,
                                onDismissRequest = {
                                    optionsExpanded = false
                                },
                                content = {
                                    options.forEach { option ->
                                        Text(
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = Spacing.m,
                                                    vertical = Spacing.s,
                                                )
                                                .clickable {
                                                    optionsExpanded = false
                                                    when (option.id) {
                                                        // TODO
                                                        OptionId.Add -> Unit

                                                        OptionId.Reset -> {
                                                            model.accept(ConfigSourcesViewModel.Intent.ResetAll)
                                                        }

                                                        else -> Unit
                                                    }
                                                },
                                            text = option.text,
                                        )
                                    }
                                },
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            items(uiState.sources) { info ->
                SourceItem(
                    sourceInfo = info,
                    options = listOf(
                        Option(
                            id = OptionId.Edit,
                            text = stringResource(R.string.action_edit),
                        ),
                        Option(
                            id = OptionId.Delete,
                            text = stringResource(R.string.action_delete),
                        ),
                    ),
                    onOptionSelected = {
                        when (it) {
                            // TODO
                            OptionId.Edit -> Unit

                            OptionId.Delete -> {
                                model.accept(ConfigSourcesViewModel.Intent.DeleteItem(info))
                            }

                            else -> Unit
                        }
                    }
                )
            }
        }
    }
}
