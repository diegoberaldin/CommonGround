package com.github.diegoberaldin.commonground.core.commonui

sealed interface OptionId {
    data object Add : OptionId
    data object Edit : OptionId
    data object Delete : OptionId
    data object Reset : OptionId
}

data class Option(
    val id: OptionId,
    val text: String,
)
