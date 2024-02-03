package com.github.diegoberaldin.commonground.feature.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
) {
    val model = injectViewModel<DefaultDrawerViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val drawerCoordinator = rememberByInjection<DrawerCoordinator>()
    val currentSource by drawerCoordinator.imageSource.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
    ) {
        items(uiState.sources) { source ->
            DrawerItem(
                modifier = Modifier.fillMaxWidth(),
                sourceInfo = source,
                active = source == currentSource,
                onSelected = {
                    drawerCoordinator.changeImageSource(source)
                    coroutineScope.launch {
                        drawerCoordinator.send(DrawerEvent.Toggle)
                    }
                }
            )
        }
    }
}
