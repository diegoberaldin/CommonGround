package com.github.diegoberaldin.commonground.domain.imagesource.usecase

import org.koin.core.annotation.Single

@Single
internal class DefaultResetSourcesUseCase(
    private val repository: com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository,
) : ResetSourcesUseCase {
    override suspend fun invoke() {
        repository.deleteAll()
        repository.create(*DEFAULT_SOURCES)
    }
}