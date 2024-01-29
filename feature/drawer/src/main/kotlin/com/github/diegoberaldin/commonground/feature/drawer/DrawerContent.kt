package com.github.diegoberaldin.commonground.feature.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    currentSourceInfo: SourceInfoModel? = null,
    onItemSelected: ((SourceInfoModel) -> Unit)? = null,
) {
    val model = injectViewModel<DefaultDrawerViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()

    LazyColumn(
        modifier = modifier,
    ) {
        items(uiState.sources) { source ->
            DrawerItem(
                modifier = Modifier.fillMaxWidth(),
                sourceInfo = source,
                active = source == currentSourceInfo,
                onSelected = {
                    onItemSelected?.invoke(source)
                }
            )
        }
    }
}
