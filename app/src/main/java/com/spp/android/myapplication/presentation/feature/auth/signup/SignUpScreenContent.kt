package com.spp.android.myapplication.presentation.feature.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedButtonStyle
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFields
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFieldsState
import com.spp.android.myapplication.presentation.designsystem.preview.FormsPreviewText
import com.spp.android.myapplication.presentation.designsystem.preview.AutoThemePreview
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.AuthPreviewText
import com.spp.android.myapplication.presentation.feature.auth.components.AuthFooter
import com.spp.android.myapplication.presentation.feature.auth.components.AuthHeader
import com.spp.android.myapplication.presentation.feature.auth.components.CheckBoxWithAction
import com.spp.android.myapplication.presentation.designsystem.components.buttons.GoogleButton

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
                title = AuthPreviewText.Register.TITLE,
                subtitle = AuthPreviewText.Register.SUBTITLE
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
                label = AuthPreviewText.Register.REMEMBER_ME
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GoogleButton(
                text = AuthPreviewText.Register.G_BUTTON,
                onClick = onRegisterWithGoogleClick
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = AuthPreviewText.Register.OR_DIVIDER,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            OutlinedBorderButton(
                text = AuthPreviewText.Register.BUTTON.uppercase(),
                onClick = onRegisterClick,
                style = OutlinedButtonStyle.Primary
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = AuthPreviewText.Register.TERMS_TEXT,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacer_small))
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            AuthFooter(
                question = AuthPreviewText.Register.FOOTER_QUESTION,
                actionText = AuthPreviewText.Register.FOOTER_ACTION,
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
                        email = FormsPreviewText.EMAIL,
                        password = FormsPreviewText.PASSWORD
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
                        email = FormsPreviewText.WRONG_EMAIL,
                        password = FormsPreviewText.WRONG_PASSWORD,
                        emailError = FormsPreviewText.Error.EMAIL,
                        passwordError = FormsPreviewText.Error.PASSWORD
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