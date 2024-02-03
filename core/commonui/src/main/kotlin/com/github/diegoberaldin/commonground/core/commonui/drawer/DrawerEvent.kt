package com.github.diegoberaldin.commonground.core.commonui.drawer

sealed interface DrawerEvent {
    data object Toggle : DrawerEvent
}