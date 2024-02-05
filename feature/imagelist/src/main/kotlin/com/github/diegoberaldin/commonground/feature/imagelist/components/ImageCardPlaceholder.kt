package com.github.diegoberaldin.commonground.feature.imagelist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.github.diegoberaldin.commonground.core.appearance.theme.CornerSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.shimmerEffect

@Composable
internal fun ImageCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(CornerSize.m)
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background, shape = shape)
            .clip(shape)
    ) {
        Box(
            modifier = modifier
                .aspectRatio(0.75f)
                .clip(shape)
                .shimmerEffect()
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = Spacing.s,
                        bottom = Spacing.s,
                    )
                    .padding(
                        end = Spacing.xxxs,
                        bottom = Spacing.xxxs,
                    ),
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }
    }
}