package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy

import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcher
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.datasource.LemmyDataSource
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.Post

class LemmyImageFetcher(
    val config: LemmyImageFetcherConfig,
    private val dataSource: LemmyDataSource,
) : ImageFetcher {

    private var pageCursor: String? = null
    private var currentPage = 1
    override var canFetchMore: Boolean = true

    override suspend fun getNextPage(): List<ImageModel> {
        if (!canFetchMore) {
            return emptyList()
        }

        val (posts, nextPage) = dataSource.getPosts(
            page = currentPage,
            pageCursor = pageCursor,
            communityName = config.community,
            host = config.host,
        ) ?: (emptyList<Post>() to null)

        pageCursor = nextPage
        if (posts.isNotEmpty()) {
            currentPage++
        } else {
            canFetchMore = false
        }

        return posts.filter {
            it.url.looksLikeAnImage
        }.mapNotNull { post ->
            post.url?.let { url ->
                ImageModel(
                    url = url,
                    title = post.name,
                )
            }
        }
    }

    override fun reset() {
        currentPage = 1
        pageCursor = null
        canFetchMore = true
    }
}

private val String?.looksLikeAnImage: Boolean
    get() {
        val imageExtensions = listOf(".jpeg", ".jpg", ".png", ".webp")
        return imageExtensions.any { this.orEmpty().endsWith(it) }
    }
