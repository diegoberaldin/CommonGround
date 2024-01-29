package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto

import kotlinx.serialization.SerialName

enum class ListingType {
    @SerialName("All")
    All,

    @SerialName("Local")
    Local,

    @SerialName("Subscribed")
    Subscribed,
}
