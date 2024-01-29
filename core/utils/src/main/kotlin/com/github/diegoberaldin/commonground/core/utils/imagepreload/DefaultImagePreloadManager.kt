package com.github.diegoberaldin.commonground.core.utils.imagepreload

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import org.koin.core.annotation.Single

@Single
class DefaultImagePreloadManager(
    private val context: Context,
) : ImagePreloadManager {

    override fun preload(url: String) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        context.imageLoader.enqueue(request)
    }
}