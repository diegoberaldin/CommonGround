package com.github.diegoberaldin.commonground.feature.drawer

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerSection
import com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository
import com.github.diegoberaldin.commonground.domain.imagesource.usecase.CreateInitialSourcesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DefaultDrawerViewModel(
    private val createInitialSources: CreateInitialSourcesUseCase,
    private val sourceInfoRepository: SourceInfoRepository,
    private val drawerCoordinator: DrawerCoordinator,
) : DrawerViewModel,
    DefaultMviModel<DrawerViewModel.Intent, DrawerViewModel.State, DrawerViewModel.Event>(
        initial = DrawerViewModel.State(),
    ) {

    override fun onCreate() {
        viewModelScope.launch {
            createInitialSources()

            sourceInfoRepository.observeAll().onEach { sources ->
                updateState { it.copy(sources = sources) }
            }.launchIn(this)

            if (drawerCoordinator.section.value == null) {
                // set first source at startup
                sourceInfoRepository.observeAll().first().firstOrNull()?.also { source ->
                    drawerCoordinator.changeSection(DrawerSection.ImageList(source))
                }
            }
        }
    }

    override fun reduce(intent: DrawerViewModel.Intent) {
    }
}