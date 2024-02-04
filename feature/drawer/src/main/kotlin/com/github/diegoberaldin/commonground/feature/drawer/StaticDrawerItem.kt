package com.github.diegoberaldin.commonground.feature.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing

@Composable
internal fun StaticDrawerItem(
    title: String,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onSelected: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelected?.invoke() }
            .padding(
                vertical = Spacing.s,
                horizontal = Spacing.m,
            ),
    ) {
        val textColor = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
        )
    }
}
