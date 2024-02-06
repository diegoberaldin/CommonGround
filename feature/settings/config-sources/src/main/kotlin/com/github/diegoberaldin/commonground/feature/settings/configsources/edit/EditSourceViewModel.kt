package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

import com.github.diegoberaldin.commonground.core.architecture.MviModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType

interface EditSourceViewModel :
    MviModel<EditSourceViewModel.Intent, EditSourceViewModel.State, EditSourceViewModel.Event> {
    sealed interface Intent {
        data class Load(val source: SourceInfoModel) : Intent
        data class ChangeTextualFieldValue(val id: EditSourceFieldId, val value: String) : Intent
        data object Submit : Intent
    }

    data class State(
        val type: SourceType = SourceType.Lemmy,
        val fields: List<EditSourceField> = emptyList(),
    )

    sealed interface Event {
        sealed interface Error : Event {
            data object MissingFields : Error
            data object InvalidSourceConfig : Error
        }

        data class Done(val source: SourceInfoModel) : Event
    }
}
