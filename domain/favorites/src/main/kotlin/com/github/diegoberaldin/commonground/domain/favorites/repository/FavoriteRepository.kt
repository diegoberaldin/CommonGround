package com.github.diegoberaldin.commonground.domain.favorites.repository

import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun isFavorite(url: String): Boolean

    suspend fun add(value: ImageModel)
    suspend fun remove(url: String)

    fun getAll(): Flow<List<ImageModel>>
}
