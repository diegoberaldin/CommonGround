package com.github.diegoberaldin.commonground.domain.gallery

sealed interface ResizeMode {
    data object Crop : ResizeMode
    data object Zoom : ResizeMode
    data object FitWidth : ResizeMode
    data object FitHeight : ResizeMode
}