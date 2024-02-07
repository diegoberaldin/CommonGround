package com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi

import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel

interface ImageSourceDescriptor {
    fun getDescription(source: SourceInfoModel): String
}