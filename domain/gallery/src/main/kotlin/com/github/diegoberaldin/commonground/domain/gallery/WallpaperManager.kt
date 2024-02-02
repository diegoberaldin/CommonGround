package com.github.diegoberaldin.commonground.domain.gallery

import android.graphics.Bitmap

interface WallpaperManager {
    fun set(bitmap: Bitmap, mode: WallpaperMode)
}
