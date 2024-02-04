package com.github.diegoberaldin.commonground.core.commonui.drawer

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
internal class DefaultDrawerCoordinator : DrawerCoordinator {
    override val events = MutableSharedFlow<DrawerEvent>()
    override val section = MutableStateFlow<DrawerSection?>(null)

    override suspend fun send(event: DrawerEvent) {
        events.emit(event)
    }

    override fun changeSection(value: DrawerSection?) {
        section.value = value
    }
}