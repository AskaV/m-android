package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun RegistrationFields(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    usernameError: String? = null,
    phoneError: String? = null
) {
    Column(modifier.fillMaxWidth()) {
        LabeledTextField(
            label = AppText.SignUp.USERNAME_LABEL.text(),
            value = username,
            onValueChange = onUsernameChange,
            kind = FieldKind.Username,
            error = usernameError
        )
        LabeledTextField(
            label = AppText.SignUp.PHONE_LABEL.text(),
            value = phone,
            onValueChange = onPhoneChange,
            kind = FieldKind.Phone,
            error = phoneError
        )
    }
}

@PreviewPhones
@Composable
fun RegistrationFieldsPreview() = PreviewColumn {
    var name by remember { mutableStateOf(AppText.Preview.USERNAME) }
    var phone by remember { mutableStateOf(AppText.Preview.PHONE) }
    RegistrationFields(
        username = name,
        onUsernameChange = { name = it },
        phone = phone,
        onPhoneChange = { phone = it })
}

@PreviewPhones
@Composable
fun RegistrationFieldsPreviewError() = PreviewColumn {
    var name by remember { mutableStateOf(AppText.Preview.WRONG_USERNAME) }
    var phone by remember { mutableStateOf(AppText.Preview.WRONG_PHONE) }
    RegistrationFields(
        username = name,
        onUsernameChange = { name = it },
        phone = phone,
        onPhoneChange = { phone = it },
        usernameError = AppText.Error.USERNAME,
        phoneError = AppText.Error.PHONE
    )
}