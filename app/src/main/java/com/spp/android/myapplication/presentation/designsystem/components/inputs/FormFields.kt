package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.FormsPreviewText
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

data class FieldSpec(
    val key: String,
    val kind: FieldKind
)

data class FieldState(
    val value: String = "",
    val error: String? = null
)

@Composable
fun FormFields(
    modifier: Modifier = Modifier,
    specs: List<FieldSpec>,
    onDone: () -> Unit = {},
    state: Map<String, FieldState>,
    onValueChange: (key: String, newValue: String) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        specs.forEachIndexed { index, spec ->
            val s = state[spec.key] ?: FieldState()
            val isLast = index == specs.lastIndex

            androidx.compose.runtime.key(spec.key) {
                LabeledTextField(
                    label = spec.kind.label,
                    value = s.value,
                    onValueChange = { onValueChange(spec.key, it) },
                    kind = spec.kind,
                    error = s.error,
                    placeholder = spec.kind.placeholderPreview,
                    imeAction = if (isLast) ImeAction.Done else spec.kind.imeAction,
                    onImeAction = if (isLast) onDone else null,
                    spacerAfter = !isLast
                )
            }
        }
    }
}

val AuthSpecs = listOf(
    FieldSpec("email", FieldKind.Email),
    FieldSpec("password", FieldKind.Password)
)

val RegistrationSpecs = listOf(
    FieldSpec("username", FieldKind.Username),
    FieldSpec("phone", FieldKind.Phone)
)


@PreviewPhones
@Composable
fun FormFieldsAuthPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = AuthSpecs,
        initialState = mapOf(
            "email" to FieldState(FormsPreviewText.EMAIL),
            "password" to FieldState(FormsPreviewText.PASSWORD)
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsAuthErrorPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = AuthSpecs,
        initialState = mapOf(
            "email" to FieldState(
                FormsPreviewText.WRONG_EMAIL,
                FormsPreviewText.Error.EMAIL
            ),
            "password" to FieldState(
                FormsPreviewText.WRONG_PASSWORD,
                FormsPreviewText.Error.PASSWORD
            )
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsRegistrationPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = RegistrationSpecs,
        initialState = mapOf(
            "username" to FieldState(FormsPreviewText.USERNAME),
            "phone" to FieldState(FormsPreviewText.PHONE)
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsRegistrationErrorPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = RegistrationSpecs,
        initialState = mapOf(
            "username" to FieldState(
                FormsPreviewText.WRONG_USERNAME,
                FormsPreviewText.Error.USERNAME
            ),
            "phone" to FieldState(
                FormsPreviewText.WRONG_PHONE,
                FormsPreviewText.Error.PHONE
            )
        )
    )
}

@Composable
private fun FormFieldsPreviewTemplate(
    specs: List<FieldSpec>,
    initialState: Map<String, FieldState>
) {
    var map by remember { mutableStateOf(initialState) }

    FormFields(
        specs = specs,
        state = map,
        onValueChange = { k, v ->
            map = map.toMutableMap().apply {
                this[k] = (this[k] ?: FieldState()).copy(
                    value = v,
                    error = null
                )
            }
        }
    )
}