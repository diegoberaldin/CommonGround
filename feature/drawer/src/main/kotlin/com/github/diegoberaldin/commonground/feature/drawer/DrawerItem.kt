package com.github.diegoberaldin.commonground.feature.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel

@Composable
internal fun DrawerItem(
    sourceInfo: SourceInfoModel,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onSelected: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clickable { onSelected?.invoke() }
            .padding(
                vertical = Spacing.s,
                horizontal = Spacing.s
            ),
    ) {
        val textColor = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Text(
            text = sourceInfo.name,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
        )

        Text(
            text = sourceInfo.type.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor.copy(0.75f),
        )
    }
}