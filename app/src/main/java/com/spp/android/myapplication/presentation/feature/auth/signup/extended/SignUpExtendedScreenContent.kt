package com.spp.android.myapplication.presentation.feature.auth.signup.extended

import android.net.Uri
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
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedButtonStyle
import com.spp.android.myapplication.presentation.designsystem.components.inputs.RegistrationFields
import com.spp.android.myapplication.presentation.designsystem.imageload.AvatarPicker
import com.spp.android.myapplication.presentation.designsystem.preview.AutoThemePreview
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.feature.auth.components.AuthHeader
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.text

data class SignUpProfileUiState(
    val username: String = "",
    val phone: String = "",
    val usernameError: String? = null,
    val phoneError: String? = null,
    val avatar: Uri? = null
)

@Composable
fun SignUpProfileScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpProfileUiState,
    onUserNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPickAvatar: () -> Unit,
    onCancel: () -> Unit,
    onForward: () -> Unit
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
            Spacer(Modifier.height(dimensionResource(R.dimen.auth_top_small)))

            AvatarPicker(
                onClick = onPickAvatar,
                showBadge = true
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_large)))

            AuthHeader(
                title = AppText.ExtendedRegister.TITLE.text(),
                subtitle = AppText.ExtendedRegister.SUBTITLE.text()
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_large)))

            RegistrationFields(
                username = state.username,
                onUsernameChange = onUserNameChange,
                phone = state.phone,
                onPhoneChange = onPhoneChange,
                usernameError = state.usernameError,
                phoneError = state.phoneError
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedBorderButton(
                text = AppText.ExtendedRegister.BUTTON.text(),
                onClick = onCancel,
                style = OutlinedButtonStyle.OnBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            OutlinedBorderButton(
                text = AppText.ExtendedRegister.BUTTON2.text(),
                onClick = onForward,
                style = OutlinedButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))
        }
    }
}


@PreviewPhones
@Composable
private fun SignUpProfileScreenPreview() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpProfileScreenContent(
                state = SignUpProfileUiState(
                    username = AppText.Preview.USERNAME,
                    phone = AppText.Preview.PHONE
                ),
                onUserNameChange = {},
                onPhoneChange = {},
                onPickAvatar = {},
                onCancel = {},
                onForward = {}
            )
        }
    }
}

@PreviewPhones
@Composable
private fun SignUpProfileScreenPreviewErrors() {
    AutoThemePreview {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpProfileScreenContent(
                state = SignUpProfileUiState(
                    username = "",
                    phone = "",
                    usernameError = AppText.Error.USERNAME_ERROR.text(),
                    phoneError = AppText.Error.PHONE_ERROR.text()
                ),
                onUserNameChange = {},
                onPhoneChange = {},
                onPickAvatar = {},
                onCancel = {},
                onForward = {}
            )
        }
    }
}