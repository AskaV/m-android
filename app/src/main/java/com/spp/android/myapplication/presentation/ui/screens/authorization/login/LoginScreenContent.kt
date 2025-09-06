package com.spp.android.myapplication.presentation.ui.screens.authorization.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.ui.preview.AutoThemePreview
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.screens.authorization.AuthPreviewText
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.AuthFooter
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.AuthHeader
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.CheckBoxWithAction
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.AuthFields
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.AuthFieldsState
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FormsPreviewText
import com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons.OutlinedButtonStyle

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit = {}
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
                title = AuthPreviewText.Login.TITLE,
                subtitle = AuthPreviewText.Login.SUBTITLE
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_large)))

            AuthFields(
                state = state.fields,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onDone = onLoginClick
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))

            CheckBoxWithAction(
                checked = state.rememberMe,
                onCheckedChange = onRememberMeChange,
                label = AuthPreviewText.Login.REMEMBER_ME,
                actionText = AuthPreviewText.Login.FORGOT_PASSWORD
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedBorderButton(
                text = AuthPreviewText.Login.BUTTON.uppercase(),
                onClick = onLoginClick,
                style = OutlinedButtonStyle.Primary
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            AuthFooter(
                question = AuthPreviewText.Login.FOOTER_QUESTION,
                actionText = AuthPreviewText.Login.FOOTER_ACTION,
                onActionClick = onNavigateToRegister
            )
        }
    }
}


@PreviewPhones
@Composable
fun LoginScreenPreview() {
    AutoThemePreview {
        androidx.compose.material3.Surface(
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            LoginScreenContent(
                state = LoginUiState(
                    fields = AuthFieldsState(
                        email = FormsPreviewText.EMAIL,
                        password = FormsPreviewText.PASSWORD
                    ),
                    rememberMe = true
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onRememberMeChange = {},
                onLoginClick = {}
            )
        }
    }
}

@PreviewPhones
@Composable
fun LoginScreenPreviewErrors() {
    AutoThemePreview {
        androidx.compose.material3.Surface(
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            LoginScreenContent(
                state = LoginUiState(
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
                onLoginClick = {}
            )
        }
    }
}