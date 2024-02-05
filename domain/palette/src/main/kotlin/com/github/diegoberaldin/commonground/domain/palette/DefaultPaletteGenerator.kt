package com.github.diegoberaldin.commonground.domain.palette

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
internal class DefaultPaletteGenerator : PaletteGenerator {
    override suspend fun generatePalette(bitmap: Bitmap): Palette = withContext(Dispatchers.IO) {
        Palette.from(bitmap).generate()
    }
}
