package com.github.diegoberaldin.commonground.core.commonui.drawer

import androidx.compose.runtime.Immutable
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface DrawerSection {
    data class ImageList(val source: SourceInfoModel) : DrawerSection
    data object Favorites : DrawerSection
    data object Settings : DrawerSection
}

@Immutable
interface DrawerCoordinator {

    val events: SharedFlow<DrawerEvent>
    val section: StateFlow<DrawerSection?>

    suspend fun send(event: DrawerEvent)

    fun changeSection(value: DrawerSection?)
}

