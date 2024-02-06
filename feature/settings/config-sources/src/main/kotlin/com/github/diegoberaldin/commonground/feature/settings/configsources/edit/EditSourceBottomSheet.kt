package com.github.diegoberaldin.commonground.feature.settings.configsources.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.utils.injectViewModel
import com.github.diegoberaldin.commonground.domain.imagesource.data.SourceInfoModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.diegoberaldin.commonground.core.commonui.R as commonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditSourceBottomSheet(
    onClose: (SourceInfoModel?) -> Unit,
    modifier: Modifier = Modifier,
    initial: SourceInfoModel? = null,
) {
    val model: EditSourceViewModel = injectViewModel<DefaultEditSourceViewModel>()
    model.BindToLifecycle()
    val uiState by model.uiState.collectAsState()
    val invalidConfigError = stringResource(commonR.string.message_invalid_fields)
    val missingFieldsError = stringResource(commonR.string.message_missing_fields)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(model) {
        if (initial != null) {
            model.accept(EditSourceViewModel.Intent.Load(initial))
        }

        model.events.onEach { event ->
            when (event) {
                is EditSourceViewModel.Event.Done -> {
                    onClose(event.source)
                }

                EditSourceViewModel.Event.Error.InvalidSourceConfig -> {
                    snackbarHostState.showSnackbar(invalidConfigError)
                }

                EditSourceViewModel.Event.Error.MissingFields -> {
                    snackbarHostState.showSnackbar(missingFieldsError)
                }
            }
        }.launchIn(this)
    }

    ModalBottomSheet(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .imePadding(),
        onDismissRequest = {
            onClose(null)
        },
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = Spacing.m)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            vertical = Spacing.s,
                            horizontal = Spacing.s,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(commonR.string.edit_source_field_type),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = uiState.type.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                for (field in uiState.fields) {
                    when (field) {
                        is EditSourceField.TextualField -> {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Ascii,
                                ),
                                label = {
                                    Text(
                                        text = field.id.toReadableName(),
                                    )
                                },
                                value = field.value,
                                onValueChange = { newValue ->
                                    model.accept(
                                        EditSourceViewModel.Intent.ChangeTextualFieldValue(
                                            id = field.id,
                                            value = newValue,
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        model.accept(EditSourceViewModel.Intent.Submit)
                    },
                ) {
                    Text(text = stringResource(commonR.string.button_ok))
                }

                Spacer(modifier = Modifier.height(Spacing.m))
            }

            SnackbarHost(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Spacing.m),
                hostState = snackbarHostState
            ) { data ->
                Snackbar(snackbarData = data)
            }
        }
    }
}
