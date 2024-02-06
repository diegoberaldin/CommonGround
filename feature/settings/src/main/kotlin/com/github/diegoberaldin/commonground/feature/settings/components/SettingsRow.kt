package com.github.diegoberaldin.commonground.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing

@Composable
fun SettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    value: String = "",
    disclosureIndicator: Boolean = false,
    annotatedValue: AnnotatedString = AnnotatedString(""),
    subtitle: String? = null,
    onTap: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clickable {
                onTap?.invoke()
            }
            .padding(
                vertical = Spacing.s,
                horizontal = Spacing.m
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        if (annotatedValue.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = Spacing.xs),
                text = annotatedValue,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        } else {
            Text(
                modifier = Modifier.padding(start = Spacing.xs),
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (disclosureIndicator) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
            )
        }
    }
}
