package com.github.diegoberaldin.commonground.domain.gallery

sealed interface WallpaperMode {
    data object LockScreen : WallpaperMode

    data object HomeScreen : WallpaperMode

    data object Both : WallpaperMode
}