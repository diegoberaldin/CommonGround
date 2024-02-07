package com.github.diegoberaldin.commonground.feature.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.l10n.localized
import com.github.diegoberaldin.commonground.domain.gallery.ResizeMode

internal fun UiTheme.toReadableString(): String = when (this) {
    UiTheme.Black -> "settings_theme_black".localized()
    UiTheme.Dark -> "settings_theme_dark".localized()
    UiTheme.Light -> "settings_theme_light".localized()
}

@Composable
internal fun UiTheme.toIcon(): ImageVector = when (this) {
    UiTheme.Black -> Icons.Default.DarkMode
    UiTheme.Dark -> Icons.Outlined.DarkMode
    UiTheme.Light -> Icons.Default.LightMode
}

internal fun ResizeMode?.toReadableString(): String = when (this) {
    ResizeMode.Crop -> "resize_mode_crop".localized()
    ResizeMode.FitHeight -> "resize_mode_fit_height".localized()
    ResizeMode.FitWidth -> "resize_mode_fit_width".localized()
    ResizeMode.Zoom -> "resize_mode_zoom".localized()
    else -> "resize_mode_none".localized()
}

internal fun String?.toLanguageName(): String = when (this) {
    "it" -> "language_it".localized()
    else -> "language_en".localized()
}