package com.github.diegoberaldin.commonground.domain.gallery

import android.graphics.Bitmap

interface ImageDownloadManager {
    suspend fun getBytes(url: String): ByteArray
    suspend fun getBitmap(url: String): Bitmap?
}
