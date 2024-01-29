package com.github.diegoberaldin.commonground.home

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import com.github.diegoberaldin.commonground.domain.imagefetch.repository.SourceInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DefaultHomeViewModel(
    private val sourceInfoRepository: SourceInfoRepository,
) : HomeViewModel,
    DefaultMviModel<HomeViewModel.Intent, HomeViewModel.State, HomeViewModel.Event>(HomeViewModel.State()) {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch(Dispatchers.IO) {
            sourceInfoRepository.observeAll().onEach { sources ->
                if (uiState.value.currentSource == null) {
                    updateCurrentSource(sources.firstOrNull())
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: HomeViewModel.Intent) {
        when (intent) {
            is HomeViewModel.Intent.SetSource -> updateCurrentSource(intent.value)
        }
    }

    private fun updateCurrentSource(source: SourceInfoModel?) {
        updateState { it.copy(currentSource = source) }
    }
}