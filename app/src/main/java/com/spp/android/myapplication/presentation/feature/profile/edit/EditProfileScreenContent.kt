package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewMoto
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.designsystem.imageload.AvatarPicker
import com.spp.android.myapplication.presentation.designsystem.preview.ProfilePreviewText
import com.spp.android.myapplication.presentation.designsystem.preview.ProfilePreviewText.EditProfile
import com.spp.android.myapplication.presentation.designsystem.components.inputs.EditProfileFields

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSave: (
        username: String,
        career: String,
        phone: String,
        address: String,
        birthdate: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val pad = dimensionResource(id = R.dimen.padding_screen)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    var username by remember { mutableStateOf(EditProfile.USERNAME) }
    var career by remember { mutableStateOf(EditProfile.CAREER) }
    var phone by remember { mutableStateOf(EditProfile.PHONE) }
    var address by remember { mutableStateOf(EditProfile.ADDRESS) }
    var birthdate by remember { mutableStateOf(EditProfile.BIRTHDATE) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = pad, vertical = spaceM)
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = ProfilePreviewText.EditProfile.EDIT_PROFILE,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AvatarPicker(onClick = { /* open picker */ }, showBadge = true)
                }

                Spacer(Modifier.height(spaceM))
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = pad)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(spaceL))

                EditProfileFields(
                    username = username, onUsernameChange = { username = it },
                    career = career, onCareerChange = { career = it },
                    phone = phone, onPhoneChange = { phone = it },
                    address = address, onAddressChange = { address = it },
                    birthdate = birthdate, onBirthdateChange = { birthdate = it },
                )

                Spacer(Modifier.height(spaceL))

                Button(
                    onClick = { onSave(username, career, phone, address, birthdate) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = ProfilePreviewText.EditProfile.SAVE.uppercase())
                }

                Spacer(Modifier.height(spaceL))
            }
        }
    }
}


@PreviewPhones
@PreviewMoto
@Composable
private fun EditProfileScreenPreview() = PreviewScreenEdgeToEdge {
    EditProfileScreen(
        onBack = {},
        onSave = { _, _, _, _, _ -> }
    )
}
