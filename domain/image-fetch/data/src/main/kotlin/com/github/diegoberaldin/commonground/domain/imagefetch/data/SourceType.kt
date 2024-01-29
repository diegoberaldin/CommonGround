package com.github.diegoberaldin.commonground.domain.imagefetch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface SourceType : Parcelable {
    @Parcelize
    data object Lemmy : SourceType
}