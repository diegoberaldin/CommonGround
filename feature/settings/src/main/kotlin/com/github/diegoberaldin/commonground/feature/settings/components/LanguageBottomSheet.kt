package com.github.diegoberaldin.commonground.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.feature.settings.toLanguageName

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun LanguageBottomSheet(
    onSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        onDismissRequest = {
            onDismiss()
        },
    ) {
        val values = listOf(
            "en",
            "it"
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            for (value in values) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected(value)
                            onDismiss()
                        }
                        .padding(
                            horizontal = Spacing.m,
                            vertical = Spacing.s
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                ) {
                    Text(
                        text = value.toLanguageName(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.m))
        }
    }
}
