package com.github.diegoberaldin.commonground.feature.drawer

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.domain.imagefetch.repository.SourceInfoRepository
import com.github.diegoberaldin.commonground.domain.imagefetch.usecase.source.CreateInitialSourcesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DefaultDrawerViewModel(
    initial: DrawerViewModel.State = DrawerViewModel.State(),
    private val createInitialSources: CreateInitialSourcesUseCase,
    private val sourceInfoRepository: SourceInfoRepository,
) : DrawerViewModel,
    DefaultMviModel<DrawerViewModel.Intent, DrawerViewModel.State, DrawerViewModel.Event>(initial) {

    override fun onCreate() {
        viewModelScope.launch {
            createInitialSources()

            sourceInfoRepository.observeAll().onEach { sources ->
                updateState { it.copy(sources = sources) }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: DrawerViewModel.Intent) {
    }
}