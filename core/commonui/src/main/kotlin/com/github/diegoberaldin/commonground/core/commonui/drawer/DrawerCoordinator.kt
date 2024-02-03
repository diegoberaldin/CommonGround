package com.github.diegoberaldin.commonground.core.commonui.drawer

import androidx.compose.runtime.Immutable
import com.github.diegoberaldin.commonground.domain.imagefetch.data.SourceInfoModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Immutable
interface DrawerCoordinator {

    val events: SharedFlow<DrawerEvent>
    val imageSource: StateFlow<SourceInfoModel?>

    suspend fun send(event: DrawerEvent)

    fun changeImageSource(value: SourceInfoModel?)
}

