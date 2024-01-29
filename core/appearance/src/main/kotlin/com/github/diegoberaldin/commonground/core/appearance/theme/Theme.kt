package com.github.diegoberaldin.commonground.core.appearance.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection

@Composable
fun CommonGroundTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val themeRepository = rememberByInjection<ThemeRepository>()
    val uiTheme by themeRepository.theme.collectAsState()
    val isLightTheme = uiTheme == UiTheme.Light

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isLightTheme) {
                dynamicLightColorScheme(context)
            } else {
                dynamicDarkColorScheme(context).apply {
                    if (uiTheme == UiTheme.Black) {
                        blackify()
                    }
                }
            }
        }

        isLightTheme -> {
            LightColorScheme
        }

        else -> {
            DarkColorScheme.apply {
                if (uiTheme == UiTheme.Black) {
                    blackify()
                }
            }
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isLightTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = content
    )
}