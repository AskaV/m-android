package com.spp.android.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.spp.android.myapplication.R

@Composable
fun EmailPasswordForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    emailError: String? = null,
    passwordError: String? = null,
    onSubmit: () -> Unit,
    showSubmitButton: Boolean = true

) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.email),
            fontSize = dimensionResource(id = R.dimen.subheading_medium_text_size).value.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        MaterialStyledTextField(
            value = email,
            onValueChange = onEmailChange,
            errorMessage = emailError
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_margin_top_small)))
        Text(
            text = stringResource(id = R.string.password),
            fontSize = dimensionResource(id = R.dimen.subheading_medium_text_size).value.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        MaterialStyledTextField(
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            errorMessage = passwordError
        )
    }
}

@Composable
fun MaterialStyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = if (isPassword)
                KeyboardOptions(keyboardType = KeyboardType.Password)
            else KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(
                fontSize = dimensionResource(id = R.dimen.heading_medium_text_size).value.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.login_button_height)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                errorCursorColor = MaterialTheme.colorScheme.onSurface,
                errorIndicatorColor = MaterialTheme.colorScheme.onSurface,
                errorContainerColor = Color.Transparent,
                errorLabelColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent

            )
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontSize = dimensionResource(id = R.dimen.body_text_size).value.sp),
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.spacer_small),
                    top = dimensionResource(id = R.dimen.spacer_extra_small)
                )
            )
        }
    }
}