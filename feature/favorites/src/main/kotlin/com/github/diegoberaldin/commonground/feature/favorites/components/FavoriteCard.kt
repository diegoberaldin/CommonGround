package com.github.diegoberaldin.commonground.feature.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.github.diegoberaldin.commonground.core.appearance.theme.CornerSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

@Composable
internal fun FavoriteCard(
    image: ImageModel,
    modifier: Modifier = Modifier,
    onFavoriteTap: () -> Unit,
) {
    val shape = RoundedCornerShape(CornerSize.m)
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondary, shape = shape)
            .clip(shape)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f),
            model = image.url,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = Spacing.s,
                    bottom = Spacing.s,
                )
                .clickable { onFavoriteTap() }
                .padding(
                    end = Spacing.xxxs,
                    bottom = Spacing.xxxs,
                ),
            imageVector = if (image.favorite) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}
