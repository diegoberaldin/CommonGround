package com.github.diegoberaldin.commonground.domain.imagefetch.cache

import androidx.collection.LruCache
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

private const val CACHE_SIZE = 5

@Single
internal class DefaultImageModelCache : ImageModelCache {
    private val cache = LruCache<String, ImageModel>(CACHE_SIZE)

    override suspend fun store(
        id: String,
        value: ImageModel,
    ): Unit = withContext(Dispatchers.IO) {
        cache.put(id, value)
    }

    override suspend fun retrieve(id: String): ImageModel? = withContext(Dispatchers.IO) {
        cache[id]
    }
}
