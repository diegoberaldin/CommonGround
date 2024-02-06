package com.github.diegoberaldin.commonground.feature.settings.configsources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.R
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

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
                )
            }
        }
    }
}

@Composable
private fun SourceItem(
    sourceInfo: SourceInfoModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = Spacing.s,
                horizontal = Spacing.m,
            ),
    ) {
        Text(
            text = sourceInfo.name,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}