package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.FilledButton
import com.spp.android.myapplication.presentation.designsystem.components.inputs.FieldState
import com.spp.android.myapplication.presentation.designsystem.components.inputs.FormFields
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.imageload.AvatarPicker
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun AddContactScreenContent(
    state: AddContactContract.State,
    onBack: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onSave: () -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onCareerChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onDobChange: (String) -> Unit = {}
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Surface(
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier.fillMaxWidth().padding(horizontal = pad, vertical = pad)
                    ) {
                        IconButton(
                            onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = AppText.Contacts.ADD.text(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        AvatarPicker(onClick = onAvatarClick, showBadge = true)
                    }

                    Spacer(Modifier.height(pad))
                }
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = pad, vertical = pad),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FormFields(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    fields = listOf(
                        FieldState(
                            value = state.username,
                            label = "Username",
                            error = state.usernameError,
                            kind = FieldKind.Username,
                            onValueChange = onUsernameChange
                        ),
                        FieldState(
                            value = state.career,
                            label = "Career",
                            error = state.careerError,
                            kind = FieldKind.Username,
                            onValueChange = onCareerChange
                        ),
                        FieldState(
                            value = state.email,
                            label = "Email",
                            error = state.emailError,
                            kind = FieldKind.Email,
                            onValueChange = onEmailChange
                        ),
                        FieldState(
                            value = state.phone,
                            label = "Phone",
                            error = state.phoneError,
                            kind = FieldKind.Phone,
                            onValueChange = onPhoneChange
                        ),
                        FieldState(
                            value = state.address,
                            label = "Address",
                            error = state.addressError,
                            kind = FieldKind.Username,
                            onValueChange = onAddressChange
                        ),
                        FieldState(
                            value = state.dateOfBirth,
                            label = "Date of birth",
                            error = state.dateOfBirthError,
                            kind = FieldKind.Username,
                            onValueChange = onDobChange
                        ),
                    ),
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    valueColor = MaterialTheme.colorScheme.onSecondary
                )

                FilledButton(
                    text = if (state.isSaving) "SAVING..." else "SAVE",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSave
                )
            }
        }
    }
}


@PreviewPhones
@Composable
private fun AddContactContentPreview() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AddContactScreenContent(
                state = AddContactContract.State(
                    username = "Jenny Walker",
                    career = "Make-up artist",
                    email = "jname@gmail.com",
                    phone = "(264)-654-3762",
                    address = "775 Westminster Avenue APT D5\nBrooklyn, NY, 11230",
                    dateOfBirth = "12/05/1995"
                )
            )
        }
    }
}