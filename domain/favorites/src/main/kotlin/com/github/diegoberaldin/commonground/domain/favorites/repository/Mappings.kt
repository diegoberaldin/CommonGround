package com.github.diegoberaldin.commonground.domain.favorites.repository

import com.github.diegoberaldin.commonground.core.persistence.entities.FavoriteEntity
import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

internal fun FavoriteEntity.toImageModel() = ImageModel(
    title = title,
    url = url,
    favorite = true,
)

internal fun ImageModel.toFavoriteEntity() = FavoriteEntity(
    url = url,
    title = title,
)