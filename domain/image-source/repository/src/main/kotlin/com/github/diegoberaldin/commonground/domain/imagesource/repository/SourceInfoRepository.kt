package com.github.diegoberaldin.commonground.domain.imagesource.repository

import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import kotlinx.coroutines.flow.Flow

interface SourceInfoRepository {
    fun observeAll(): Flow<List<SourceInfoModel>>

    suspend fun getById(id: Int): SourceInfoModel?

    suspend fun update(value: SourceInfoModel)

    suspend fun create(vararg value: SourceInfoModel)

    suspend fun delete(value: SourceInfoModel)
}