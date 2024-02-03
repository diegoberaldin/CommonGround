package com.github.diegoberaldin.commonground.domain.imagefetch.usecase.source

import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.createAsString
import com.github.diegoberaldin.commonground.domain.imagefetch.repository.SourceInfoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

private val DEFAULT_SOURCES =
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
        SourceInfoModel(
            name = "Accidental Renaissance",
            type = SourceType.Lemmy,
            config = createAsString("lemmy.world", "AccidentalRenaissance@kbin.social"),
        ),
    )

@Single
internal class DefaultCreateInitialSourcesUseCase(
    private val repository: SourceInfoRepository,
) : CreateInitialSourcesUseCase {
    override suspend fun invoke() {
        val existing = repository.observeAll().first()
        if (existing.isEmpty()) {
            repository.create(*DEFAULT_SOURCES)
        }
    }
}
