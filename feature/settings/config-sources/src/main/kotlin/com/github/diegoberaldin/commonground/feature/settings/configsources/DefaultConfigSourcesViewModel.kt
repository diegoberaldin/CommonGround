package com.github.diegoberaldin.commonground.feature.settings.configsources

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageSourceDescriptor
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository
import com.github.diegoberaldin.commonground.domain.imagesource.usecase.ResetSourcesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultConfigSourcesViewModel(
    private val sourceInfoRepository: SourceInfoRepository,
    private val resetSources: ResetSourcesUseCase,
    private val sourceDescriptor: ImageSourceDescriptor,
) : ConfigSourcesViewModel,
    DefaultMviModel<ConfigSourcesViewModel.Intent, ConfigSourcesViewModel.State, ConfigSourcesViewModel.Event>(
        initial = ConfigSourcesViewModel.State(),
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            sourceInfoRepository.observeAll().onEach { list ->
                updateState {
                    it.copy(
                        sources = list.map { source ->
                            val description = sourceDescriptor.getDescription(source)
                            source.copy(description = description)
                        }
                    )
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: ConfigSourcesViewModel.Intent) {
        when (intent) {
            is ConfigSourcesViewModel.Intent.DeleteItem -> {
                delete(intent.source)
            }

            ConfigSourcesViewModel.Intent.ResetAll -> resetAll()
            is ConfigSourcesViewModel.Intent.Upsert -> upsertSource(intent.source)
        }
    }

    private fun delete(sourceInfo: SourceInfoModel) {
        viewModelScope.launch {
            sourceInfoRepository.delete(sourceInfo)
        }
    }

    private fun resetAll() {
        viewModelScope.launch {
            resetSources()
        }
    }

    private fun upsertSource(source: SourceInfoModel) {
        viewModelScope.launch {
            sourceInfoRepository.create(source)
        }
    }
}
