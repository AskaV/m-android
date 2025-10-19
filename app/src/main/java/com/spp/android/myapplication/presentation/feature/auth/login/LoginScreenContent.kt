package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedButtonStyle
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFields
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFieldsState
import com.spp.android.myapplication.presentation.designsystem.preview.AutoThemePreview
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.feature.auth.components.AuthFooter
import com.spp.android.myapplication.presentation.feature.auth.components.AuthHeader
import com.spp.android.myapplication.presentation.feature.auth.components.CheckBoxWithAction
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginContract.State,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRememberMeChange: (Boolean) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val hPad = dimensionResource(R.dimen.spacer_medium)

    Box(
        modifier = modifier.fillMaxSize().padding(start = hPad, end = hPad, bottom = hPad)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.auth_top_spacer)))

            AuthHeader(
                title = AppText.Login.TITLE.text(), subtitle = AppText.Login.SUBTITLE.text()
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_large)))

            AuthFields(
                state = AuthFieldsState(
                    email = state.email,
                    password = state.password,
                    emailErrorKey = state.emailErrorKey,
                    passwordErrorKey = state.passwordErrorKey
                ),
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onDone = onLoginClick
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            CheckBoxWithAction(
                checked = state.rememberMe,
                onCheckedChange = onRememberMeChange,
                label = AppText.Login.REMEMBER_ME.text(),
                actionText = AppText.Login.FORGOT_PASSWORD.text(),
                onActionClick = onForgotPasswordClick
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedBorderButton(
                text = AppText.Login.LOGIN.text().uppercase(),
                onClick = onLoginClick,
                style = OutlinedButtonStyle.Primary,
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            AuthFooter(
                question = AppText.Login.DONT_HAVE_ACCOUNT.text(),
                actionText = AppText.Login.SIGN_UP.text(),
                onActionClick = onNavigateToRegister
            )
        }
    }
}

@PreviewPhones
@Composable
fun LoginScreenPreview() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreenContent(
                state = LoginContract.State(
                    email = AppText.Preview.EMAIL,
                    password = AppText.Preview.PASSWORD,
                    rememberMe = true
                )
            )
        }
    }
}

@PreviewPhones
@Composable
fun LoginScreenPreviewErrors() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreenContent(
                state = LoginContract.State(
                    email = AppText.Preview.WRONG_EMAIL,
                    password = AppText.Preview.WRONG_PASSWORD,
                    emailErrorKey = AppText.Login.EMAIL_ERROR,
                    passwordErrorKey = AppText.Error.PASSWORD_ERROR
                )
            )
        }
    }
}