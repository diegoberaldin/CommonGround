package com.github.diegoberaldin.commonground.feature.drawer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

@Composable
internal fun ImageListDrawerItem(
    source: SourceInfoModel,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onSelected: (() -> Unit)? = null,
) {
    NavigationDrawerItem(
        modifier = modifier,
        label = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Text(
                    text = source.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                if (source.description.isNotEmpty()) {
                    Text(
                        text = source.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light,
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(0.75f)
                    )
                }
            }
        },
        selected = active,
        onClick = {
            onSelected?.invoke()
        },
    )
}
