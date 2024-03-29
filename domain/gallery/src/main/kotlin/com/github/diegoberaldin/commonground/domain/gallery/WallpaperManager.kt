package com.github.diegoberaldin.commonground.domain.gallery

import android.graphics.Bitmap

interface WallpaperManager {
    suspend fun set(
        bitmap: Bitmap,
        wallpaperMode: WallpaperMode,
        resizeMode: ResizeMode? = null,
    )
}
