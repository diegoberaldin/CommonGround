package com.github.diegoberaldin.commonground.feature.imagedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.github.diegoberaldin.commonground.core.appearance.theme.CornerSize
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing

@Composable
internal fun OverlayBox(
    title: String,
    modifier: Modifier = Modifier,
    onSave: (() -> Unit)? = null,
    onSet: (() -> Unit)? = null,
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
            onSave = {
                onSave?.invoke()
            },
            onSet = {
                onSet?.invoke()
            },
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
    }
}

@Composable
private fun CommandBar(
    onSave: () -> Unit,
    onSet: () -> Unit,
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
            modifier = buttonModifier,
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}