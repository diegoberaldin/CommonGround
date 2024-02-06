package com.github.diegoberaldin.commonground.feature.settings.configsources

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultConfigSourcesViewModel(
    private val sourceInfoRepository: SourceInfoRepository,
) : ConfigSourcesViewModel,
    DefaultMviModel<ConfigSourcesViewModel.Intent, ConfigSourcesViewModel.State, ConfigSourcesViewModel.Event>(
        initial = ConfigSourcesViewModel.State(),
    ) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            sourceInfoRepository.observeAll().onEach { list ->
                updateState { it.copy(sources = list) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: ConfigSourcesViewModel.Intent) {
        when (intent) {
            else -> Unit
        }
    }
}
