package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherimpl

import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageSourceDescriptor
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.toLemmyConfig
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType
import org.koin.core.annotation.Single

@Single
class DefaultImageSourceDescriptor : ImageSourceDescriptor {
    override fun getDescription(source: SourceInfoModel): String {
        return when (source.type) {
            SourceType.Lemmy -> buildString {
                val lemmyConfig = source.config.toLemmyConfig() ?: return@buildString
                append("!")
                append(lemmyConfig.community)
                append("@")
                append(lemmyConfig.host)
            }

            else -> ""
        }
    }
}