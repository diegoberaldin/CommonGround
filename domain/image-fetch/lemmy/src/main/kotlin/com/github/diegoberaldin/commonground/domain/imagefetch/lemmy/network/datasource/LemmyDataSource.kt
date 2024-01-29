package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.datasource

import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.ListingType
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.Post
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto.SortType

interface LemmyDataSource {
    suspend fun getPosts(
        page: Int? = null,
        pageCursor: String? = null,
        limit: Int = 20,
        type: ListingType = ListingType.Local,
        sort: SortType = SortType.Active,
        communityName: String,
        host: String,
    ): Pair<List<Post>, String?>?
}

