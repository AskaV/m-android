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
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.t
import com.spp.android.myapplication.presentation.texts.text

enum class FieldId { EMAIL, PASSWORD, USERNAME, PHONE }

data class FieldSpec(
    val id: FieldId,
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
    state: Map<FieldId, FieldState>,
    onValueChange: (id: FieldId, newValue: String) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        specs.forEachIndexed { index, spec ->
            val s = state[spec.id] ?: FieldState()
            val isLast = index == specs.lastIndex

            androidx.compose.runtime.key(spec.id) {
                LabeledTextField(
                    label = spec.kind.label,
                    value = s.value,
                    onValueChange = { onValueChange(spec.id, it) },
                    kind = spec.kind,
                    error = s.error,
                    imeAction = if (isLast) ImeAction.Done else spec.kind.imeAction,
                    onImeAction = if (isLast) onDone else null,
                    spacerAfter = !isLast
                )
            }
        }
    }
}

val AuthSpecs = listOf(
    FieldSpec(FieldId.EMAIL, FieldKind.Email),
    FieldSpec(FieldId.PASSWORD, FieldKind.Password)
)

val RegistrationSpecs = listOf(
    FieldSpec(FieldId.USERNAME, FieldKind.Username),
    FieldSpec(FieldId.PHONE, FieldKind.Phone)
)


@PreviewPhones
@Composable
fun FormFieldsAuthPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = AuthSpecs,
        initialState = mapOf(
            FieldId.EMAIL to FieldState(AppText.Preview.EMAIL),
            FieldId.PASSWORD to FieldState(AppText.Preview.PASSWORD)
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsAuthErrorPreview() = PreviewColumn {

    FormFieldsPreviewTemplate(
        specs = AuthSpecs,
        initialState = mapOf(
            FieldId.EMAIL to FieldState(
                AppText.Preview.WRONG_EMAIL,
                AppText.Login.EMAIL_ERROR.text()
            ),
            FieldId.PASSWORD to FieldState(
                AppText.Preview.WRONG_PASSWORD,
                AppText.Login.PASSWORD_ERROR_TEMPLATE.t(AppText.Integers.PASSWORD_MIN_LENGTH)
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
            FieldId.USERNAME to FieldState(AppText.Preview.USERNAME),
            FieldId.PHONE to FieldState(AppText.Preview.PHONE)
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsRegistrationErrorPreview() = PreviewColumn {
    FormFieldsPreviewTemplate(
        specs = RegistrationSpecs,
        initialState = mapOf(
            FieldId.USERNAME to FieldState(
                value = AppText.Preview.WRONG_USERNAME,          
                error = AppText.Error.USERNAME_ERROR.text()
            ),
            FieldId.PHONE to FieldState(
                value = AppText.Preview.WRONG_PHONE,             
                error = AppText.Error.PHONE_ERROR.text()
            )
        )
    )
}

@Composable
private fun FormFieldsPreviewTemplate(
    specs: List<FieldSpec>,
    initialState: Map<FieldId, FieldState>
) {
    var map by remember { mutableStateOf(initialState) }

    FormFields(
        specs = specs,
        state = map,
        onValueChange = { id, v ->
            map = map.toMutableMap().apply {
                val current = this[id] ?: FieldState()
                this[id] = current.copy(value = v, error = null)
            }
        }
    )
}