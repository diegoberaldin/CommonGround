package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

import com.github.diegoberaldin.commonground.core.l10n.localized

sealed interface EditSourceFieldId {
    data object UserDefinedName : EditSourceFieldId
    data object LemmyCommunityName : EditSourceFieldId
    data object LemmyHostName : EditSourceFieldId
}

fun EditSourceFieldId.toReadableName(): String = when (this) {
    EditSourceFieldId.UserDefinedName -> "edit_source_field_name".localized()
    EditSourceFieldId.LemmyHostName -> "edit_source_field_lemmy_host".localized()
    EditSourceFieldId.LemmyCommunityName -> "edit_source_field_lemmy_community".localized()
}