package com.github.diegoberaldin.commonground.domain.palette

import androidx.collection.LruCache
import androidx.palette.graphics.Palette
import org.koin.core.annotation.Single

@Single
internal class DefaultPaletteCache : PaletteCache {

    private val cache = LruCache<String, Palette>(2)

    override suspend fun get(url: String): Palette? = cache[url]

    override suspend fun put(url: String, palette: Palette) {
        cache.put(url, palette)
    }
}
