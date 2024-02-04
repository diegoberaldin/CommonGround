package com.github.diegoberaldin.commonground.domain.imagesource.usecase

import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
internal class DefaultCreateInitialSourcesUseCase(
    private val repository: com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository,
) : CreateInitialSourcesUseCase {
    override suspend fun invoke() {
        val existing = repository.observeAll().first()
        if (existing.isEmpty()) {
            repository.create(*DEFAULT_SOURCES)
        }
    }
}