package com.github.diegoberaldin.commonground.domain.favorites.repository

import com.github.diegoberaldin.commonground.core.persistence.provider.DaoProvider
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
internal class DefaultFavoriteRepository(
    private val daoProvider: DaoProvider,
) : FavoriteRepository {
    override suspend fun isFavorite(url: String): Boolean = withContext(Dispatchers.IO) {
        daoProvider.favorite.getByUrl(url) != null
    }

    override suspend fun add(value: ImageModel) = withContext(Dispatchers.IO) {
        if (isFavorite(value.url)) {
            return@withContext
        }

        daoProvider.favorite.insert(value.toFavoriteEntity())
    }

    override suspend fun remove(url: String) = withContext(Dispatchers.IO) {
        daoProvider.favorite.delete(url)
    }

    override fun getAll(): Flow<List<ImageModel>> =
        daoProvider.favorite.getAll().map { it.map { f -> f.toImageModel() } }
            .flowOn(Dispatchers.IO)
}
