package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Community(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("removed") val removed: Boolean,
    @SerialName("published") val published: String,
    @SerialName("updated") val updated: String? = null,
    @SerialName("deleted") val deleted: Boolean,
    @SerialName("nsfw") val nsfw: Boolean,
    @SerialName("actor_id") val actorId: String,
    @SerialName("local") val local: Boolean,
    @SerialName("icon") val icon: String? = null,
    @SerialName("banner") val banner: String? = null,
    @SerialName("hidden") val hidden: Boolean,
    @SerialName("posting_restricted_to_mods") val postingRestrictedToMods: Boolean,
    @SerialName("instance_id") val instanceId: Int,
)