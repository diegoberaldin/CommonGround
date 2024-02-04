package com.github.diegoberaldin.commonground.domain.imagesource.repository

import com.github.diegoberaldin.commonground.core.persistence.entities.SourceInfoEntity
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType

internal fun Int.toSourceType(): SourceType = when (this) {
    0 -> SourceType.Lemmy
    else -> error("Invalid source type")
}

internal fun SourceType.toInt(): Int = when (this) {
    SourceType.Lemmy -> 0
    else -> -1
}

internal fun SourceInfoEntity.toModel() =
    SourceInfoModel(
        id = id,
        name = name,
        type = type.toSourceType(),
        config = config,
    )

internal fun SourceInfoModel.toEntity() = SourceInfoEntity(
    id = id ?: 0,
    name = name,
    type = type.toInt(),
    config = config
)
