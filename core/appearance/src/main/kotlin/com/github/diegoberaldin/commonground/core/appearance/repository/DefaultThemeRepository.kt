package com.github.diegoberaldin.commonground.core.appearance.repository

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
internal class DefaultThemeRepository : ThemeRepository {

    override val theme = MutableStateFlow<UiTheme?>(null)

    override fun changeTheme(value: UiTheme) {
        theme.value = value
    }
}