package com.spp.android.myapplication.presentation.designsystem.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

@JvmOverloads
@Composable
fun EditProfileFields(
    modifier: Modifier = Modifier,
    username: String = "",
    onUsernameChange: (String) -> Unit = {},
    career: String = "",
    onCareerChange: (String) -> Unit = {},
    phone: String = "",
    onPhoneChange: (String) -> Unit = {},
    address: String = "",
    onAddressChange: (String) -> Unit = {},
    birthdate: String = "",
    onBirthdateChange: (String) -> Unit = {},
    usernameError: String = "",
    careerError: String = "",
    phoneError: String = "",
    addressError: String = "",
    birthdateError: String = "",
) {
    Surface(color = MaterialTheme.colorScheme.surface) {

        Column(modifier.fillMaxWidth()) {
            LabeledTextField(
                label = AppText.EditProfile.USERNAME_LABEL.text(),
                value = username,
                onValueChange = onUsernameChange,
                kind = FieldKind.Username,
                error = usernameError,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            LabeledTextField(
                label = AppText.EditProfile.CAREER_LABEL.text(),
                value = career,
                onValueChange = onCareerChange,
                kind = FieldKind.Username,
                error = careerError,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            LabeledTextField(
                label = AppText.EditProfile.PHONE_LABEL.text(),
                value = phone,
                onValueChange = onPhoneChange,
                kind = FieldKind.Phone,
                error = phoneError,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            LabeledTextField(
                label = AppText.EditProfile.ADDRESS_LABEL.text(),
                value = address,
                onValueChange = onAddressChange,
                kind = FieldKind.Username,
                error = addressError,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            LabeledTextField(
                label = AppText.EditProfile.BIRTHDATE_LABEL.text(),
                value = birthdate,
                onValueChange = onBirthdateChange,
                kind = FieldKind.Username,
                error = birthdateError,
                imeAction = ImeAction.Done,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }
}


@PreviewPhones
@Composable
fun EditProfileFieldsPreview() = PreviewColumn {
    var username by rememberText(AppText.EditProfile.USERNAME)
    var career by rememberText(AppText.EditProfile.CAREER)
    var phone by rememberText(AppText.EditProfile.PHONE)
    var address by rememberText(AppText.EditProfile.ADDRESS)
    var birth by rememberText(AppText.EditProfile.BIRTHDATE)

    EditProfileFields(
        username = username, onUsernameChange = { username = it },
        career = career, onCareerChange = { career = it },
        phone = phone, onPhoneChange = { phone = it },
        address = address, onAddressChange = { address = it },
        birthdate = birth, onBirthdateChange = { birth = it },
    )
}

@Composable
private fun rememberText(key: TextKey) = run {
    val initial = key.text()
    remember(initial) { mutableStateOf(initial) }
}