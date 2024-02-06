package com.github.diegoberaldin.commonground.domain.settings

import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.persistence.entities.SettingsEntity
import com.github.diegoberaldin.commonground.core.persistence.provider.DaoProvider
import com.github.diegoberaldin.commonground.domain.gallery.ResizeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
internal class DefaultSettingsRepository(
    private val daoProvider: DaoProvider,
) : SettingsRepository {

    override val current = MutableStateFlow(SettingsModel())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            daoProvider.settings.get()?.also {
                current.value = it.toModel()
            }
        }
    }

    override suspend fun update(value: SettingsModel) = withContext(Dispatchers.IO) {
        daoProvider.settings.insert(value.toEntity())
    }
}

private fun SettingsEntity.toModel() = SettingsModel(
    theme = theme?.toUiTheme(),
    resizeMode = resizeMode?.toResizeMode(),
)

private fun SettingsModel.toEntity() = SettingsEntity(
    id = 1,
    theme = theme?.toInt(),
    resizeMode = resizeMode?.toInt(),
)

private fun UiTheme.toInt(): Int = when (this) {
    UiTheme.Light -> 0
    UiTheme.Dark -> 1
    UiTheme.Black -> 2
}

private fun Int.toUiTheme(): UiTheme = when (this) {
    2 -> UiTheme.Black
    1 -> UiTheme.Dark
    else -> UiTheme.Light
}

private fun ResizeMode.toInt(): Int = when (this) {
    ResizeMode.Crop -> 0
    ResizeMode.FitHeight -> 1
    ResizeMode.FitWidth -> 2
    ResizeMode.Zoom -> 3
}

private fun Int.toResizeMode(): ResizeMode = when (this) {
    1 -> ResizeMode.FitHeight
    2 -> ResizeMode.FitWidth
    3 -> ResizeMode.Zoom
    else -> ResizeMode.Crop
}
