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
import com.spp.android.myapplication.presentation.texts.TextKey
import com.spp.android.myapplication.presentation.texts.TextKeyWithArgs
import com.spp.android.myapplication.presentation.texts.text

data class AuthFieldsState(
    val email: String = "",
    val password: String = "",
    val emailErrorKey    : TextKey? = null,
    val passwordErrorKey : TextKeyWithArgs? = null,
)

@Composable
fun AuthFields(
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
    state: AuthFieldsState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {}
) {
    Column(modifier.fillMaxWidth()) {
        LabeledTextField(
            label = AppText.Login.EMAIL_LABEL.text(),
            value = state.email,
            onValueChange = onEmailChange,
            kind = FieldKind.Email,
            error = state.emailErrorKey?.text().orEmpty(),
            imeAction = ImeAction.Next
        )
        LabeledTextField(
            label = AppText.Login.PASSWORD_LABEL.text(),
            value = state.password,
            onValueChange = onPasswordChange,
            kind = FieldKind.Password,
            error = state.passwordErrorKey?.text().orEmpty(),
            imeAction = ImeAction.Done,
            onImeAction = onDone,
            spacerAfter = false
        )
    }
}

@PreviewPhones
@Composable
fun AuthFieldsPreview() = PreviewColumn {
    var email by remember { mutableStateOf(AppText.Preview.EMAIL) }
    var pass by remember { mutableStateOf(AppText.Preview.DOTS) }
    AuthFields(
        state = AuthFieldsState(email, pass),
        onEmailChange = { email = it },
        onPasswordChange = { pass = it })
}

@PreviewPhones
@Composable
fun AuthFieldsPreviewError() = PreviewColumn {
    var email by remember { mutableStateOf(AppText.Preview.WRONG_EMAIL) }
    var pass by remember { mutableStateOf(AppText.Preview.WRONG_PASSWORD) }
    AuthFields(
        state = AuthFieldsState(
        email = email,
        password = pass,
            emailErrorKey = AppText.Login.EMAIL_ERROR,
            passwordErrorKey = AppText.Error.PASSWORD_ERROR
    ), onEmailChange = { email = it }, onPasswordChange = { pass = it }, onDone = {})
}