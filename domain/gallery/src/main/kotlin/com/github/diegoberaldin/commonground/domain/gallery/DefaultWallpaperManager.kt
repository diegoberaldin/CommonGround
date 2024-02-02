package com.github.diegoberaldin.commonground.domain.gallery

import android.content.Context
import android.graphics.Bitmap
import org.koin.core.annotation.Single

@Single
internal class DefaultWallpaperManager(
    private val context: Context,
) : WallpaperManager {
    override fun set(bitmap: Bitmap, mode: WallpaperMode) {
        TODO("Not yet implemented")
    }
}
