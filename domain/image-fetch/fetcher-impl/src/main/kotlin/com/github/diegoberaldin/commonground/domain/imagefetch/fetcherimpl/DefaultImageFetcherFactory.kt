package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherimpl

import com.github.diegoberaldin.commonground.core.utils.getByInjection
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.toLemmyConfig
import org.koin.core.annotation.Single

@Single
internal class DefaultImageFetcherFactory :
    com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcherFactory {
    override fun create(info: SourceInfoModel): com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcher =
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