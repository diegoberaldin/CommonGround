package com.github.diegoberaldin.commonground.core.commonui.drawer

import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
internal class DefaultDrawerCoordinator : DrawerCoordinator {
    override val events = MutableSharedFlow<DrawerEvent>()
    override val imageSource = MutableStateFlow<SourceInfoModel?>(null)

    override suspend fun send(event: DrawerEvent) {
        events.emit(event)
    }

    override fun changeImageSource(value: SourceInfoModel?) {
        imageSource.value = value
    }
}