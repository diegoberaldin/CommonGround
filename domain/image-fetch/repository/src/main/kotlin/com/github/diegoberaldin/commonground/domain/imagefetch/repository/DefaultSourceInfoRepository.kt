package com.github.diegoberaldin.commonground.domain.imagefetch.repository

import com.github.diegoberaldin.commonground.core.persistence.provider.DaoProvider
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
internal class DefaultSourceInfoRepository(
    private val daoProvider: DaoProvider,
) : SourceInfoRepository {

    override fun observeAll(): Flow<List<SourceInfoModel>> {
        return daoProvider.sourceInfo.getAll().map { it.map { e -> e.toModel() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        daoProvider.sourceInfo.getById(id)?.toModel()
    }

    override suspend fun update(value: SourceInfoModel) = withContext(Dispatchers.IO) {
        val entity = value.toEntity()
        daoProvider.sourceInfo.update(entity)
    }

    override suspend fun create(vararg value: SourceInfoModel) = withContext(Dispatchers.IO) {
        val entities = value.map { it.toEntity() }.toTypedArray()
        daoProvider.sourceInfo.insert(*entities)
    }

    override suspend fun delete(value: SourceInfoModel) {
        val entity = value.toEntity()
        daoProvider.sourceInfo.delete(entity)
    }
}