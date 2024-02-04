package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi

import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

interface ImageFetcherFactory {
    fun create(info: SourceInfoModel): ImageFetcher
}
