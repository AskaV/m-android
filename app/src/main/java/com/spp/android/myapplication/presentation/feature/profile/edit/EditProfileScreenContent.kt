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
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewMoto
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun EditProfileScreenContent(
    modifier: Modifier = Modifier,
    state: EditProfileContract.State = EditProfileContract.State(),
    onBack: () -> Unit = {},
    onValueChange: (field: String, value: String) -> Unit = { _, _ -> },
    onSave: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)
    val buttonHeight = dimensionResource(id = R.dimen.button_height)

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier.fillMaxWidth().padding(horizontal = pad, vertical = spaceM),
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = AppText.EditProfile.TITLE.text(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AvatarPicker(onClick = { /* TODO open picker */ }, showBadge = true)
                }

                Spacer(Modifier.height(spaceM))
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = pad),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = buttonHeight + pad),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(spaceL))

                    FormFields(
                        fields =
                            listOf(
                                FieldState(
                                    value = state.username,
                                    label = AppText.EditProfile.USERNAME_LABEL.text(),
                                    error = state.usernameErrorKey?.text() ?: "",
                                    kind = FieldKind.Username,
                                    onValueChange = { onValueChange("username", it) },
                                ),
                                FieldState(
                                    value = state.career,
                                    label = AppText.EditProfile.CAREER_LABEL.text(),
                                    error = "",
                                    kind = FieldKind.Username,
                                    onValueChange = { onValueChange("career", it) },
                                ),
                                FieldState(
                                    value = state.phone,
                                    label = AppText.EditProfile.PHONE_LABEL.text(),
                                    error = state.phoneErrorKey?.text() ?: "",
                                    kind = FieldKind.Phone,
                                    onValueChange = { onValueChange("phone", it) },
                                ),
                                FieldState(
                                    value = state.address,
                                    label = AppText.EditProfile.ADDRESS_LABEL.text(),
                                    error = "",
                                    kind = FieldKind.Username,
                                    onValueChange = { onValueChange("address", it) },
                                ),
                                FieldState(
                                    value = state.birthdate,
                                    label = AppText.EditProfile.BIRTHDATE_LABEL.text(),
                                    error = "",
                                    kind = FieldKind.Username,
                                    onValueChange = { onValueChange("birthdate", it) },
                                ),
                            ),
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        valueColor = MaterialTheme.colorScheme.onSecondary,
                    )

                    Spacer(Modifier.height(spaceL))
                }

                FilledButton(
                    text = AppText.EditProfile.SAVE.text(),
                    onClick = {
                        onSave(
                            state.username,
                            state.career,
                            state.phone,
                            state.address,
                            state.birthdate,
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = pad),
                )
            }
        }
    }
}

@PreviewPhones
@PreviewMoto
@Composable
private fun EditProfileScreenPreview() = PreviewScreenEdgeToEdge { EditProfileScreenContent() }
