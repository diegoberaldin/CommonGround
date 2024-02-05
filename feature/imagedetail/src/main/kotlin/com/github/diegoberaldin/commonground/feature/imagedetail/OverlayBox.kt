package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.github.diegoberaldin.commonground.core.appearance.theme.CornerSize
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing

@Composable
internal fun OverlayBox(
    title: String,
    favorite: Boolean,
    previewColors: List<Color>,
    modifier: Modifier = Modifier,
    onSave: (() -> Unit)? = null,
    onSet: (() -> Unit)? = null,
    onToggleFavorite: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(
                vertical = Spacing.m,
                horizontal = Spacing.m,
            )
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background.copy(0.5f),
                shape = RoundedCornerShape(CornerSize.l),
            )
            .padding(
                vertical = Spacing.m,
                horizontal = Spacing.m,
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        CommandBar(
            favorite = favorite,
            onSave = {
                onSave?.invoke()
            },
            onSet = {
                onSet?.invoke()
            },
            onToggleFavorite = {
                onToggleFavorite?.invoke()
            },
            onShare = {
                onShare?.invoke()
            },
        )
        PalettePreview(
            colors = previewColors,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(Spacing.xxs))
    }
}

@Composable
private fun CommandBar(
    favorite: Boolean,
    onSave: () -> Unit,
    onSet: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        val buttonModifier = Modifier
            .size(IconSize.l)
            .padding(Spacing.xxs)
        Icon(
            modifier = buttonModifier.clickable {
                onShare()
            },
            imageVector = Icons.Default.Share,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
        Icon(
            modifier = buttonModifier.clickable {
                onSave()
            },
            imageVector = Icons.Default.SaveAlt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
        Icon(
            modifier = buttonModifier.clickable {
                onSet()
            },
            imageVector = Icons.Default.Wallpaper,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
        Icon(
            modifier = buttonModifier.clickable {
                onToggleFavorite()
            },
            imageVector = if (favorite) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun PalettePreview(
    colors: List<Color>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        for (color in colors) {
            Box(
                modifier = Modifier
                    .size(IconSize.m)
                    .background(
                        color = color,
                        shape = CircleShape,
                    )
            )
        }
    }
}