package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.SocialButtonsRow
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.feature.profile.components.FilledBtn
import com.spp.android.myapplication.presentation.feature.profile.components.OutlinedBtn
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomArea
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileHeader
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: MyProfileContract.State,
    onEditProfile: () -> Unit = {},
    onViewContacts: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.spacer_large)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
                        .padding(bottom = dimensionResource(id = R.dimen.spacer_large))
                ) {
                    Text(
                        text = AppText.MyProfile.SETTINGS.text(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        border = BorderStroke(
                            width = dimensionResource(id = R.dimen.button_border_width),
                            color = MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Text(text = AppText.MyProfile.LOGOUT.text())
                    }
                }

                ProfileHeader(
                    name = state.name,
                    linePrimary = state.linePrimary,
                    lineSecondary = state.lineSecondary
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_large)))
            }
        }

        val padding = dimensionResource(id = R.dimen.spacer_medium)

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = padding).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isCompleted) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SocialButtonsRow()
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

            ProfileBottomArea(
                showSocial = state.isCompleted,
                primaryFilled = FilledBtn(
                    text = AppText.MyProfile.VIEW_CONTACTS.text(), onClick = onViewContacts
                ),
                secondaryOutlined = OutlinedBtn(
                    text = AppText.MyProfile.EDIT_PROFILE.text(), onClick = onEditProfile
                ),
                hint = if (!state.isCompleted) AppText.MyProfile.PROFILE_FILL_HINT.text() else null,
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(horizontal = padding)
            )
        }
    }
}

@PreviewPhones
@Composable
private fun ProfileScreenPreviewIncomplete() = PreviewScreenEdgeToEdge {
    ProfileScreen(
        state = MyProfileContract.State(
            name = AppText.MyProfile.NAME.text(),
            linePrimary = AppText.MyProfile.CAREER.text(),
            lineSecondary = AppText.MyProfile.ADDRESS.text(),
            isCompleted = false
        )
    )
}

@PreviewPhones
@Composable
private fun ProfileScreenPreviewCompleted() = PreviewScreenEdgeToEdge {
    ProfileScreen(
        state = MyProfileContract.State(
            name = AppText.MyProfileDetailed.NAME.text(),
            linePrimary = AppText.MyProfileDetailed.CAREER.text(),
            lineSecondary = AppText.MyProfileDetailed.ADDRESS.text(),
            isCompleted = true
        )
    )
}