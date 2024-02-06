package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

sealed interface EditSourceField {
    val id: EditSourceFieldId

    data class TextualField(
        override val id: EditSourceFieldId,
        val value: String,
    ) : EditSourceField
}
