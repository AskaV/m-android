package com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.spp.android.myapplication.presentation.ui.preview.PreviewColumn
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FieldKind
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FormsPreviewText
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.inputs.LabeledTextField

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
            label = FormsPreviewText.Label.USERNAME,
            value = username,
            onValueChange = onUsernameChange,
            kind = FieldKind.Username,
            error = usernameError,
            placeholder = FormsPreviewText.USERNAME
        )
        LabeledTextField(
            label = FormsPreviewText.Label.PHONE,
            value = phone,
            onValueChange = onPhoneChange,
            kind = FieldKind.Phone,
            error = phoneError,
            placeholder = FormsPreviewText.PHONE
        )
    }
}

@PreviewPhones
@Composable
fun RegistrationFieldsPreview() = PreviewColumn {
    var name by remember { mutableStateOf(FormsPreviewText.USERNAME) }
    var phone by remember { mutableStateOf(FormsPreviewText.PHONE) }
    RegistrationFields(
        username = name,
        onUsernameChange = { name = it },
        phone = phone,
        onPhoneChange = { phone = it })
}

@PreviewPhones
@Composable
fun RegistrationFieldsPreviewError() = PreviewColumn {
    var name by remember { mutableStateOf(FormsPreviewText.WRONG_USERNAME) }
    var phone by remember { mutableStateOf(FormsPreviewText.WRONG_PHONE) }
    RegistrationFields(
        username = name,
        onUsernameChange = { name = it },
        phone = phone,
        onPhoneChange = { phone = it },
        usernameError = FormsPreviewText.Error.USERNAME,
        phoneError = FormsPreviewText.Error.PHONE
    )
}