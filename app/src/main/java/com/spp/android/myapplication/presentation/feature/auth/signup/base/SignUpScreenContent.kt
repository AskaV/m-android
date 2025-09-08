package com.spp.android.myapplication.presentation.feature.auth.signup.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.GoogleButton
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

data class SignUpUiState(
    val fields: AuthFieldsState = AuthFieldsState(),
    val rememberMe: Boolean = false
)

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onRegisterWithGoogleClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val hPad = dimensionResource(R.dimen.spacer_medium)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = hPad, end = hPad, bottom = hPad)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.auth_top_spacer)))

            AuthHeader(
                title = AppText.SignUp.TITLE.text(),
                subtitle = AppText.SignUp.SUBTITLE.text()
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_large)))

            AuthFields(
                state = state.fields,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onDone = onRegisterClick
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            CheckBoxWithAction(
                checked = state.rememberMe,
                onCheckedChange = onRememberMeChange,
                label = AppText.Login.REMEMBER_ME.text()
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GoogleButton(
                text = AppText.SignUp.GOOGLE.text().uppercase(),
                onClick = onRegisterWithGoogleClick
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = AppText.SignUp.OR.text().uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            OutlinedBorderButton(
                text = AppText.SignUp.REGISTER.text().uppercase(),
                onClick = onRegisterClick,
                style = OutlinedButtonStyle.Primary
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = AppText.SignUp.TERMS.text(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacer_small))
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            AuthFooter(
                question = AppText.SignUp.HAVE_ACCOUNT.text(),
                actionText = AppText.SignUp.SIGN_IN.text(),
                onActionClick = onNavigateToLogin
            )
        }
    }
}


@PreviewPhones
@Composable
fun SignUpScreenPreview() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpScreenContent(
                state = SignUpUiState(
                    fields = AuthFieldsState(
                        email = AppText.Preview.EMAIL,
                        password = AppText.Preview.PASSWORD
                    ),
                    rememberMe = true
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onRememberMeChange = {},
                onRegisterClick = {}
            )
        }
    }
}

@PreviewPhones
@Composable
fun SignUpScreenPreviewErrors() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpScreenContent(
                state = SignUpUiState(
                    fields = AuthFieldsState(
                        email = AppText.Preview.WRONG_EMAIL,
                        password = AppText.Preview.WRONG_PASSWORD,
                        emailError = AppText.Login.EMAIL_ERROR.text(),
                        passwordError = AppText.Error.PASSWORD
                    ),
                    rememberMe = false
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onRememberMeChange = {},
                onRegisterClick = {}
            )
        }
    }
}