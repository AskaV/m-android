package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

enum class FieldId {
    EMAIL, PASSWORD, USERNAME, PHONE
}

data class FieldSpec(
    val id: FieldId, val kind: FieldKind
)

data class FieldState(
    val value: String = "",
    val label: String = "",
    val error: String = "",
    val kind: FieldKind,
    val onValueChange: (newValue: String) -> Unit = {}
)

@Composable
fun FormFields(
    modifier: Modifier = Modifier,
    fields: List<FieldState> = emptyList(),
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    valueColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    Column(modifier.fillMaxWidth()) {
        fields.forEachIndexed { index, fieldState ->
            LabeledTextField(
                label = fieldState.label,
                value = fieldState.value,
                onValueChange = fieldState.onValueChange,
                kind = fieldState.kind,
                error = fieldState.error,
                spacerAfter = index != fields.lastIndex,
                labelTextColor = labelColor,
                valueTextColor = valueColor
            )
        }
    }
}

val AuthSpecs = listOf(
    FieldSpec(
        FieldId.EMAIL, FieldKind.Email
    ), FieldSpec(
        FieldId.PASSWORD, FieldKind.Password
    )
)

val RegistrationSpecs = listOf(
    FieldSpec(
        FieldId.USERNAME, FieldKind.Username
    ), FieldSpec(
        FieldId.PHONE, FieldKind.Phone
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
            ), FieldState(
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
            ), FieldState(
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
        fields = previewFields
    )
}

private val previewFields = listOf(
    FieldState("Jenny Walker", "Username", "", FieldKind.Username),
    FieldState("Make-up artist", "Career", "", FieldKind.Username),
    FieldState("jname@gmail.com", "Email", "", FieldKind.Email),
    FieldState("(264)-654-3762", "Phone", "", FieldKind.Phone),
    FieldState(
        "775 Westminster Avenue APT D5\nBrooklyn, NY, 11230", "Address", "", FieldKind.Username
    ),
    FieldState("12/05/1995", "Date of birth", "", FieldKind.Username)
)