package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageFetcherFactory
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.createAsString
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.LemmyImageFetcherConfigBuilder.toLemmyConfig
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceType
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultEditSourceViewModel(
    private val imageFetcherFactory: ImageFetcherFactory,
) : EditSourceViewModel,
    DefaultMviModel<EditSourceViewModel.Intent, EditSourceViewModel.State, EditSourceViewModel.Event>(
        initial = EditSourceViewModel.State()
    ) {

    private var editedSourceId: Int? = null
    override fun onCreate() {
        super.onCreate()
        generateFields(SourceType.Lemmy)
    }

    override fun reduce(intent: EditSourceViewModel.Intent) {
        when (intent) {

            is EditSourceViewModel.Intent.ChangeTextualFieldValue -> {
                updateFieldValue(intent.id, intent.value)
            }

            is EditSourceViewModel.Intent.Load -> {
                val source = intent.source
                editedSourceId = source.id
                val type = source.type
                updateState { it.copy(type = type) }
                generateFields(type)

                updateFieldValue(EditSourceFieldId.UserDefinedName, source.name)
                when (type) {
                    SourceType.Lemmy -> {
                        loadLemmyConfig(source.config)
                    }
                }
            }

            EditSourceViewModel.Intent.Submit -> submit()
        }
    }

    private fun updateFieldValue(fieldId: EditSourceFieldId, value: String) {
        updateState { oldState ->
            oldState.copy(
                fields = oldState.fields.map { field ->
                    when {
                        field.id == fieldId && field is EditSourceField.TextualField -> {
                            field.copy(value = value)
                        }

                        else -> field
                    }
                }
            )
        }
    }

    private fun generateFields(type: SourceType) {
        updateState {
            it.copy(
                type = type,
                fields = listOf(
                    EditSourceField.TextualField(
                        id = EditSourceFieldId.UserDefinedName,
                        value = ""
                    ),
                    EditSourceField.TextualField(
                        id = EditSourceFieldId.LemmyCommunityName,
                        value = ""
                    ),
                    EditSourceField.TextualField(
                        id = EditSourceFieldId.LemmyHostName,
                        value = ""
                    ),
                )
            )
        }
    }

    private fun loadLemmyConfig(config: String) {
        val lemmyConfig = config.toLemmyConfig()
        updateFieldValue(
            fieldId = EditSourceFieldId.LemmyHostName,
            value = lemmyConfig?.host.orEmpty()
        )
        updateFieldValue(
            fieldId = EditSourceFieldId.LemmyCommunityName,
            value = lemmyConfig?.community.orEmpty()
        )
    }

    private fun extractFieldValue(fieldId: EditSourceFieldId): String =
        uiState.value.fields.firstOrNull {
            it.id == fieldId
        }?.let {
            it as? EditSourceField.TextualField
        }?.value.orEmpty()

    private fun submit() {
        val type = uiState.value.type
        val name = extractFieldValue(EditSourceFieldId.UserDefinedName)
        val config = when (type) {
            SourceType.Lemmy -> {
                val hostName = extractFieldValue(EditSourceFieldId.LemmyHostName)
                val communityName = extractFieldValue(EditSourceFieldId.LemmyCommunityName)
                createAsString(
                    host = hostName,
                    community = communityName,
                )
            }

            else -> ""
        }

        if (name.isEmpty() || config.isEmpty()) {
            emit(EditSourceViewModel.Event.Error.MissingFields)
            return
        }

        val source = SourceInfoModel(
            id = editedSourceId ?: 0,
            type = type,
            name = name,
            config = config,
        )
        val fetcher = imageFetcherFactory.create(source)
        viewModelScope.launch {
            val images = fetcher.getNextPage()
            if (images.isEmpty()) {
                emit(EditSourceViewModel.Event.Error.InvalidSourceConfig)
                return@launch
            }

            emit(EditSourceViewModel.Event.Done(source))
        }
    }
}
