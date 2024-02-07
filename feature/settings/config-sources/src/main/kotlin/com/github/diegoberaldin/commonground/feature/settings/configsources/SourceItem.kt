package com.github.diegoberaldin.commonground.feature.settings.configsources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import com.github.diegoberaldin.commonground.core.appearance.theme.IconSize
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.Option
import com.github.diegoberaldin.commonground.core.commonui.OptionId
import com.github.diegoberaldin.commonground.core.utils.toLocalDp
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

@Composable
internal fun SourceItem(
    sourceInfo: SourceInfoModel,
    modifier: Modifier = Modifier,
    options: List<Option> = emptyList(),
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    var optionsExpanded by remember { mutableStateOf(false) }
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = Spacing.s,
                horizontal = Spacing.s,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.padding(start = Spacing.s),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = sourceInfo.name,
                style = MaterialTheme.typography.bodyLarge,
                color = fullColor,
            )
            if (sourceInfo.description.isNotEmpty()) {
                Text(
                    text = sourceInfo.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                    ),
                    color = ancillaryColor
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (options.isNotEmpty()) {
            Box {
                Icon(
                    modifier = Modifier
                        .size(IconSize.l)
                        .padding(Spacing.xs)
                        .padding(top = Spacing.xxs)
                        .onGloballyPositioned {
                            optionsOffset = it.positionInParent()
                        }
                        .clickable {
                            optionsExpanded = true
                        },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = ancillaryColor,
                )

                DropdownMenu(
                    modifier = Modifier,
                    offset = DpOffset(
                        x = optionsOffset.x.toLocalDp(),
                        y = optionsOffset.y.toLocalDp(),
                    ),
                    expanded = optionsExpanded,
                    onDismissRequest = {
                        optionsExpanded = false
                    },
                    content = {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                },
                                onClick = {
                                    optionsExpanded = false
                                    onOptionSelected?.invoke(option.id)
                                }
                            )
                        }
                    },
                )
            }
        }
    }
}