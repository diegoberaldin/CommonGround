package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.github.diegoberaldin.commonground.core.utils.injectViewModel

@Composable
fun ImageDetailScreen(
    id: String,
) {
    val model = injectViewModel<DefaultImageDetailViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()

    LaunchedEffect(id) {
        model.accept(ImageDetailViewModel.Intent.Load(id))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = uiState.url,
            contentDescription = uiState.title,
            contentScale = ContentScale.FillBounds,
        )
    }
}