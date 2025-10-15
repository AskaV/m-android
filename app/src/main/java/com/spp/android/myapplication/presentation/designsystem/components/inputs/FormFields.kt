package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

enum class FieldId {
    EMAIL,
    PASSWORD,
    USERNAME,
    PHONE
}

data class FieldSpec(
    val id: FieldId,
    val kind: FieldKind
)

data class FieldState(
    val value: String = "",
    val label: String = "",
    val error: String = "",
    val kind: FieldKind,
    val onValueChange: (newValue: String) -> Unit = { }
)

@Composable
fun FormFields(
    modifier: Modifier = Modifier,
    fields: List<FieldState> = emptyList()
) {
    Column(modifier.fillMaxWidth()) {
        fields.forEachIndexed { index, fieldState ->
            LabeledTextField(
                label = fieldState.label,
                value = fieldState.value,
                onValueChange = fieldState.onValueChange,
                kind = fieldState.kind,
                error = fieldState.error,
                spacerAfter = index != fields.lastIndex
            )
        }
    }
}

val AuthSpecs = listOf(
    FieldSpec(
        FieldId.EMAIL,
        FieldKind.Email
    ),
    FieldSpec(
        FieldId.PASSWORD,
        FieldKind.Password
    )
)

val RegistrationSpecs = listOf(
    FieldSpec(
        FieldId.USERNAME,
        FieldKind.Username
    ),
    FieldSpec(
        FieldId.PHONE,
        FieldKind.Phone
    )
)


@PreviewPhones
@Composable
fun FormFieldsAuthPreview() = PreviewColumn {
    FormFields(
        fields = listOf(
            FieldState(
                value = "em@g.com",
                label = "Email",
                error = "",
                kind = FieldKind.Email,
            ),
            FieldState(
                value = "asdasdas",
                label = "password",
                error = "",
                kind = FieldKind.Password,
            )
        )
    )
}

@PreviewPhones
@Composable
fun FormFieldsRegistrationPreview() = PreviewColumn {
    FormFields(
        fields = listOf(
            FieldState(
                value = "user",
                label = "username",
                error = "",
                kind = FieldKind.Username,
            ),
            FieldState(
                value = "+380646584654",
                label = "phone",
                error = "",
                kind = FieldKind.Phone,
            )
        )
    )
}


@PreviewPhones
@Composable
fun FormFieldsEditP() = PreviewColumn {
    FormFields(
        fields = listOf(
            FieldState(
                value = "user",
                label = "username",
                error = "",
                kind = FieldKind.Username,
            ),
            FieldState(
                value = "careed",
                label = "car",
                error = "",
                kind = FieldKind.Username,
            ),
            FieldState(
                value = "+380646584654",
                label = "phone",
                error = "",
                kind = FieldKind.Phone,
            ),
            FieldState(
                value = "adresa",
                label = "address",
                error = "",
                kind = FieldKind.Username,
            ),
            FieldState(
                value = "10/10/10",
                label = "dateOfBirth",
                error = "",
                kind = FieldKind.Username,
            )
        )
    )
}