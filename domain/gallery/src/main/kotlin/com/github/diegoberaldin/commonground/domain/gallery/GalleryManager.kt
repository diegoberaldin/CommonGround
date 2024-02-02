package com.github.diegoberaldin.commonground.domain.gallery

import android.graphics.Bitmap

interface GalleryManager {
    suspend fun saveToGallery(bytes: ByteArray)
    suspend fun saveToGallery(bitmap: Bitmap)
}
