package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LemmyImageFetcherConfig(
    @SerialName("hostName") val host: String,
    @SerialName("communityName") val community: String,
)


object LemmyImageFetcherConfigBuilder {

    fun createAsString(host: String, community: String): String = LemmyImageFetcherConfig(
        host = host,
        community = community
    ).let {
        Json.encodeToString(it)
    }

    fun String.toLemmyConfig(): LemmyImageFetcherConfig? {
        return runCatching {
            Json.decodeFromString<LemmyImageFetcherConfig>(this)
        }.getOrNull()
    }
}