package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherimpl

import com.github.diegoberaldin.commonground.core.utils.getByInjection
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcherFactory
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.toLemmyConfig
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType
import org.koin.core.annotation.Single

@Single
internal class DefaultImageFetcherFactory :
    ImageFetcherFactory {
    override fun create(info: SourceInfoModel): ImageFetcher =
        when (info.type) {
            SourceType.Lemmy -> {
                val config = info.config.toLemmyConfig()
                require(config != null)
                LemmyImageFetcher(
                    config = config,
                    dataSource = getByInjection()
                )
            }

            else -> error("Unknown source type ${info.type}")
        }
}