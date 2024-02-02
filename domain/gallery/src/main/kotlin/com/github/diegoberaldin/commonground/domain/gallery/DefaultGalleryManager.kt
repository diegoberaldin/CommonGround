package com.github.diegoberaldin.commonground.domain.gallery

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.util.UUID

@Single
internal class DefaultGalleryManager(
    private val context: Context,
) : GalleryManager {

    override suspend fun saveToGallery(bytes: ByteArray) = withContext(Dispatchers.IO) {
        val name = UUID.randomUUID().toString()
        val resolver = context.applicationContext.contentResolver

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val details = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = resolver.insert(collection, details)
        if (uri != null) {
            resolver.openFileDescriptor(uri, "w", null).use { pfd ->
                ParcelFileDescriptor.AutoCloseOutputStream(pfd).use {
                    it.write(bytes)
                }
            }
            details.clear()
            details.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, details, null, null)
        }
    }

    override suspend fun saveToGallery(bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val name = UUID.randomUUID().toString() + ".png"
        val resolver = context.applicationContext.contentResolver

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val details = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = resolver.insert(collection, details)
        if (uri != null) {
            resolver.openFileDescriptor(uri, "w", null).use { pfd ->
                ParcelFileDescriptor.AutoCloseOutputStream(pfd).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
            }
            details.clear()
            details.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, details, null, null)
        }
    }
}