package com.github.diegoberaldin.commonground.core.appearance.repository

sealed interface UiTheme {
    data object Light : UiTheme

    data object Dark : UiTheme

    data object Black : UiTheme
}