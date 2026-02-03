package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.TextKeyWithArgs
import com.spp.android.myapplication.presentation.texts.text

@Composable
fun RegistrationFields(
    modifier: Modifier = Modifier,
    username: String = "",
    onUsernameChange: (String) -> Unit = {},
    phone: String = "",
    onPhoneChange: (String) -> Unit = {},
    usernameErrorKey: TextKeyWithArgs? = null,
    phoneErrorKey: TextKeyWithArgs? = null,
) {
    Column(modifier.fillMaxWidth()) {
        LabeledTextField(
            label = AppText.SignUp.USERNAME_LABEL.text(),
            value = username,
            onValueChange = onUsernameChange,
            kind = FieldKind.Username,
            error = usernameErrorKey?.text().orEmpty(),
        )
        LabeledTextField(
            label = AppText.SignUp.PHONE_LABEL.text(),
            value = phone,
            onValueChange = onPhoneChange,
            kind = FieldKind.Phone,
            error = phoneErrorKey?.text().orEmpty(),
        )
    }
}

@PreviewPhones
@Composable
fun RegistrationFieldsPreview() =
    PreviewColumn {
        RegistrationFields(
            username = AppText.Preview.USERNAME,
            phone = AppText.Preview.PHONE,
        )
    }

@PreviewPhones
@Composable
fun RegistrationFieldsPreviewError() =
    PreviewColumn {
        RegistrationFields(
            username = AppText.Preview.WRONG_USERNAME,
            phone = AppText.Preview.WRONG_PHONE,
            usernameErrorKey = AppText.Error.USERNAME_ERROR,
            phoneErrorKey = AppText.Error.PHONE_ERROR,
        )
    }
