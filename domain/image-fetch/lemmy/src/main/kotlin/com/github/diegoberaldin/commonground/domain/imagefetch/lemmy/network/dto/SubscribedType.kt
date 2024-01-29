package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.dto

import kotlinx.serialization.SerialName

enum class SubscribedType {
    @SerialName("Subscribed")
    Subscribed,

    @SerialName("NotSubscribed")
    NotSubscribed,

    @SerialName("Pending")
    Pending,
}