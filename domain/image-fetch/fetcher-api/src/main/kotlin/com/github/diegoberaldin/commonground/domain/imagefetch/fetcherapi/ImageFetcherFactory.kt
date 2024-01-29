package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi

import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel

interface ImageFetcherFactory {
    fun create(info: SourceInfoModel): ImageFetcher
}
