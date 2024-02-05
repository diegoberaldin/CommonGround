package com.github.diegoberaldin.commonground.domain.palette

import androidx.palette.graphics.Palette

interface PaletteCache {
    suspend fun get(url: String): Palette?

    suspend fun put(url: String, palette: Palette)
}
