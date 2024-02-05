package com.github.diegoberaldin.commonground.domain.palette

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

interface PaletteGenerator {
    suspend fun generatePalette(bitmap: Bitmap): Palette
}
