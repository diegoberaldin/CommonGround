package com.github.diegoberaldin.commonground.core.appearance.repository

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

    val theme: StateFlow<UiTheme?>

    fun changeTheme(value: UiTheme)
}
