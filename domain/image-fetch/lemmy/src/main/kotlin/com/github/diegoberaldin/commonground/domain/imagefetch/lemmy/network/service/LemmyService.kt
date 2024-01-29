package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.service

import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.GetPostsResponse
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.ListingType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.SortType
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PostService {

    @GET("post/list")
    suspend fun getAll(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: SortType? = null,
        @Query("page") page: Int? = null,
        @Query("page_cursor") pageCursor: String? = null,
        @Query("type_") type: ListingType? = null,
        @Query("community_id") communityId: Int? = null,
        @Query("community_name") communityName: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): GetPostsResponse
}