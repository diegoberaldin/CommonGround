package com.github.diegoberaldin.commonground.domain.imagesource.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourceInfoModel(
    val id: Int? = null,
    val name: String,
    val type: SourceType,
    val config: String = "",
) : Parcelable
