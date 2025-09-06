package com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.ui.preview.PreviewColumn
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FieldKind
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FormsPreviewText
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.inputs.LabeledTextField

data class AuthFieldsState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)

@Composable
fun AuthFields(
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
    state: AuthFieldsState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        LabeledTextField(
            label = stringResource(R.string.email),
            value = state.email,
            onValueChange = onEmailChange,
            kind = FieldKind.Email,
            error = state.emailError,
            placeholder = FormsPreviewText.EMAIL,
            imeAction = ImeAction.Next
        )
        LabeledTextField(
            label = stringResource(R.string.password),
            value = state.password,
            onValueChange = onPasswordChange,
            kind = FieldKind.Password,
            error = state.passwordError,
            placeholder = FormsPreviewText.DOTS,
            imeAction = ImeAction.Done,
            onImeAction = onDone,
            spacerAfter = false
        )
    }
}

@PreviewPhones
@Composable
fun AuthFieldsPreview() = PreviewColumn {
    var email by remember { mutableStateOf(FormsPreviewText.EMAIL) }
    var pass by remember { mutableStateOf(FormsPreviewText.PASSWORD) }
    AuthFields(
        state = AuthFieldsState(email, pass),
        onEmailChange = { email = it },
        onPasswordChange = { pass = it }
    )
}

@PreviewPhones
@Composable
fun AuthFieldsPreviewError() = PreviewColumn {
    var email by remember { mutableStateOf(FormsPreviewText.WRONG_EMAIL) }
    var pass by remember { mutableStateOf(FormsPreviewText.WRONG_PASSWORD) }
    AuthFields(
        state = AuthFieldsState(
            email = email,
            password = pass,
            emailError = FormsPreviewText.Error.EMAIL,
            passwordError = FormsPreviewText.Error.PASSWORD
        ),
        onEmailChange = { email = it },
        onPasswordChange = { pass = it },
        onDone = {}
    )
}