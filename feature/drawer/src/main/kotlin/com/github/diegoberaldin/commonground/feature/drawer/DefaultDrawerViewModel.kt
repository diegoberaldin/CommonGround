package com.github.diegoberaldin.commonground.feature.drawer

import androidx.lifecycle.viewModelScope
import com.github.diegoberaldin.commonground.core.architecture.DefaultMviModel
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerSection
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherapi.ImageSourceDescriptor
import com.github.diegoberaldin.commonground.domain.imagesource.repository.SourceInfoRepository
import com.github.diegoberaldin.commonground.domain.imagesource.usecase.CreateInitialSourcesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DefaultDrawerViewModel(
    private val createInitialSources: CreateInitialSourcesUseCase,
    private val sourceInfoRepository: SourceInfoRepository,
    private val drawerCoordinator: DrawerCoordinator,
    private val sourceDescriptor: ImageSourceDescriptor,
) : DrawerViewModel,
    DefaultMviModel<DrawerViewModel.Intent, DrawerViewModel.State, DrawerViewModel.Event>(
        initial = DrawerViewModel.State(),
    ) {

    private var firstLoad = true

    override fun onCreate() {
        viewModelScope.launch {
            createInitialSources()

            sourceInfoRepository.observeAll().onEach { sources ->
                if (firstLoad) {
                    firstLoad = false
                    val source = sources.firstOrNull()
                    if (source != null) {
                        drawerCoordinator.changeSection(DrawerSection.ImageList(source))
                    }
                }

                updateState {
                    it.copy(
                        sources = sources.map { sourceInfo ->
                            val description = sourceDescriptor.getDescription(sourceInfo)
                            sourceInfo.copy(description = description)
                        },
                    )
                }
            }.launchIn(this)
        }
    }

    override fun reduce(intent: DrawerViewModel.Intent) {
    }
}