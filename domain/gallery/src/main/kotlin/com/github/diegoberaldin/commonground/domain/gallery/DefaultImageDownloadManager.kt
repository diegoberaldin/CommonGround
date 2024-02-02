package com.github.diegoberaldin.commonground.domain.gallery

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.annotation.Single
import java.io.ByteArrayOutputStream

@Single
internal class DefaultImageDownloadManager(
    private val context: Context,
) : ImageDownloadManager {
    override suspend fun getBytes(url: String): ByteArray = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        val ostream = ByteArrayOutputStream()
        response.body?.byteStream()?.use {
            it.copyTo(ostream)
        }
        ostream.toByteArray()
    }

    override suspend fun getBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        val result = context.imageLoader.execute(request)
        result.drawable?.toBitmap()
    }
}