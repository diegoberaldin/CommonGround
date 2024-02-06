package com.github.diegoberaldin.commonground.domain.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import android.app.WallpaperManager as AndroidWallpaperManager

/*
 * CREDITS:
 * https://github.com/you-apps/WallYou/blob/ed2d91e41689824b50ba361ee22a4549ad8c859c/app/src/main/java/com/bnyro/wallpaper/util/WallpaperHelper.kt
 */

@Single
internal class DefaultWallpaperManager(
    private val context: Context,
) : WallpaperManager {

    override suspend fun set(
        bitmap: Bitmap,
        wallpaperMode: WallpaperMode,
        resizeMode: ResizeMode?,
    ) = withContext(Dispatchers.IO) {
        val result = run {
            resizeBitmap(bitmap, resizeMode)
        }

        if (wallpaperMode in listOf(WallpaperMode.Both, WallpaperMode.HomeScreen)) {
            setWallpaperUp(result, AndroidWallpaperManager.FLAG_SYSTEM)
        }
        if (wallpaperMode in listOf(WallpaperMode.Both, WallpaperMode.LockScreen)) {
            setWallpaperUp(result, AndroidWallpaperManager.FLAG_LOCK)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setWallpaperUp(imageBitmap: Bitmap, mode: Int) {
        val (width, height) = getMetrics()
        val wallpaperManager = AndroidWallpaperManager.getInstance(context)
        wallpaperManager.suggestDesiredDimensions(width, height)

        if (!wallpaperManager.isWallpaperSupported) return
        wallpaperManager.setBitmap(imageBitmap, null, true, mode)
    }

    private fun resizeBitmap(bitmap: Bitmap, resizeMode: ResizeMode? = null): Bitmap {
        val (width, height) = getMetrics()

        return when (resizeMode) {
            ResizeMode.Crop -> getResizedBitmap(bitmap, width, height)
            ResizeMode.Zoom -> getZoomedBitmap(bitmap, width, height)
            ResizeMode.FitWidth -> getBitmapFitWidth(bitmap, width)
            ResizeMode.FitHeight -> getBitmapFitHeight(bitmap, height)
            else -> bitmap
        }
    }

    private fun getResizedBitmap(
        bitmap: Bitmap,
        width: Int,
        height: Int,
        filter: Boolean = true
    ): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, filter)
    }

    private fun getZoomedBitmap(bitmap: Bitmap, screenWidth: Int, screenHeight: Int): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
        val screenRatio = screenHeight.toFloat() / screenWidth.toFloat()
        val scaleRatio = (bitmapRatio / screenRatio)

        var (newWidth, newHeight) = bitmap.width to bitmap.height
        if (bitmapRatio > screenRatio) {
            newHeight = (scaleRatio * bitmap.height).toInt()
        } else {
            newWidth = (scaleRatio * bitmap.width).toInt()
        }

        val gapX = (bitmap.width - newWidth) / 2
        val gapY = (bitmap.height - newHeight) / 2
        val centeredBitmap = Bitmap.createBitmap(bitmap, gapX, gapY, newWidth, newHeight)

        return getResizedBitmap(centeredBitmap, screenWidth, screenHeight)
    }

    private fun getBitmapFitWidth(bitmap: Bitmap, width: Int): Bitmap {
        val heightRatio = width.toFloat() / bitmap.width.toFloat()

        return getResizedBitmap(bitmap, width, (bitmap.height * heightRatio).toInt())
    }

    private fun getBitmapFitHeight(bitmap: Bitmap, height: Int): Bitmap {
        val widthRatio = height.toFloat() / bitmap.height.toFloat()

        return getResizedBitmap(bitmap, (bitmap.width * widthRatio).toInt(), height)
    }

    private fun getMetrics(): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.let { it.width() to it.height() }
        } else {
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(metrics)
            metrics.widthPixels to metrics.heightPixels
        }
    }
}
