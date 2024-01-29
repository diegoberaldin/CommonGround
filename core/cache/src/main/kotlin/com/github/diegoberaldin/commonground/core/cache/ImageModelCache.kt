package com.github.diegoberaldin.commonground.core.cache

import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

interface ImageModelCache {
    suspend fun store(id: String, value: ImageModel)
    suspend fun retrieve(id: String): ImageModel?
}
