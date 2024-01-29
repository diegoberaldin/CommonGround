package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostAggregates(
    @SerialName("id") val id: Int? = null,
    @SerialName("post_id") val postId: Int,
    @SerialName("comments") val comments: Int,
    @SerialName("score") val score: Int,
    @SerialName("upvotes") val upvotes: Int,
    @SerialName("downvotes") val downvotes: Int,
    @SerialName("published") val published: String,
    @SerialName("newest_comment_time_necro") val newestCommentTimeNecro: String? = null,
    @SerialName("newest_comment_time") val newestCommentTime: String? = null,
    @SerialName("featured_community") val featuredCommunity: Boolean? = null,
    @SerialName("featured_local") val featuredLocal: Boolean? = null,
    @SerialName("hot_rank") val hotRank: Float? = null,
    @SerialName("hot_rank_active") val hotRankActive: Float? = null,
)