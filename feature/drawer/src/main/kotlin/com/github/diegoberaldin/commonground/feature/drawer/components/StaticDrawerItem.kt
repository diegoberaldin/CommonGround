package com.github.diegoberaldin.commonground.feature.drawer.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun StaticDrawerItem(
    title: String,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onSelected: (() -> Unit)? = null,
) {
    NavigationDrawerItem(
        modifier = modifier,
        label = {
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
        },
        selected = active,
        onClick = {
            onSelected?.invoke()
        },
    )
}
