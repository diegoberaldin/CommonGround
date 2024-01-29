package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi

import com.github.diegoberaldin.commonground.domain.imagefetch.data.ImageModel

interface ImageFetcher {

    val canFetchMore: Boolean
    suspend fun getNextPage(): List<ImageModel>

    fun reset()
}
