package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.diegoberaldin.commonground.core.commonui.R as commonR

sealed interface EditSourceFieldId {
    data object UserDefinedName : EditSourceFieldId
    data object LemmyCommunityName : EditSourceFieldId
    data object LemmyHostName : EditSourceFieldId
}

@Composable
fun EditSourceFieldId.toReadableName(): String = when (this) {
    EditSourceFieldId.UserDefinedName -> stringResource(commonR.string.edit_source_field_name)
    EditSourceFieldId.LemmyHostName -> stringResource(commonR.string.edit_source_field_lemmy_host)
    EditSourceFieldId.LemmyCommunityName -> stringResource(commonR.string.edit_source_field_lemmy_community)
}