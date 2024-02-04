package com.github.diegoberaldin.commonground.domain.imagesource.usecase

import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.createAsString
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType

internal val DEFAULT_SOURCES =
    arrayOf(
        SourceInfoModel(
            name = "ApocalypticArt",
            type = SourceType.Lemmy,
            config = createAsString("feddit.de", "apocalypticart"),
        ),
        SourceInfoModel(
            name = "AI Generated Images",
            type = SourceType.Lemmy,
            config = createAsString("sh.itjust.works", "imageai"),
        ),
        SourceInfoModel(
            name = "Stable Diffusion Art",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.dbzer0.com", "stable_diffusion_art"),
        ),
        SourceInfoModel(
            name = "EarthPorn",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.world", "earthporn"),
        ),
        SourceInfoModel(
            name = "Digital Art",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.world", "digitalart"),
        ),
        SourceInfoModel(
            name = "Pics",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.world", "pics"),
        ),
        SourceInfoModel(
            name = "Traditional Art",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.world", "traditional_art"),
        ),
    )
