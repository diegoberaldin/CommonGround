package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.datasource

import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.ListingType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.Post
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.SortType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.service.ServiceProvider
import org.koin.core.annotation.Single

@Single
internal class DefaultLemmyDataSource(
    private val services: ServiceProvider,
) : LemmyDataSource {
    override suspend fun getPosts(
        page: Int?,
        pageCursor: String?,
        limit: Int,
        type: ListingType,
        sort: SortType,
        communityName: String,
        host: String,
    ): Pair<List<Post>, String?>? = runCatching {
        services.changeInstance(host)
        val response = services.post.getAll(
            communityName = communityName,
            page = if (pageCursor.isNullOrEmpty()) page else null,
            pageCursor = pageCursor,
            limit = limit,
            type = type,
            sort = sort,
        )
        val posts = response.posts.map { it.post }
        posts to response.nextPage
    }.getOrNull()
}