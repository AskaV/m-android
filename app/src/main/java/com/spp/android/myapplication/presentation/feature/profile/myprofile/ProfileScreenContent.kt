package com.spp.android.myapplication.presentation.feature.profile.myprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomArea
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomState
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileHeader
import com.spp.android.myapplication.presentation.designsystem.preview.ProfilePreviewText
import com.spp.android.myapplication.presentation.designsystem.components.buttons.SocialButtonsRow

data class ProfileUiState(
    val name: String,
    val linePrimary: String,
    val lineSecondary: String,
    val isCompleted: Boolean
)

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onViewContacts: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.spacer_large)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_screen))
                        .padding(bottom = dimensionResource(id = R.dimen.spacer_large))
                ) {
                    Text(
                        text = ProfilePreviewText.MyProfile.SETTINGS,
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
                        Text(text = ProfilePreviewText.MyProfile.LOGOUT)
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

        val pad = dimensionResource(id = R.dimen.padding_screen)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = pad)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SocialButtonsRow()
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

            val bottomState: ProfileBottomState =
                if (state.isCompleted) ProfileBottomState.MyProfileCompleted
                else ProfileBottomState.MyProfileIncomplete(
                    hint = ProfilePreviewText.MyProfile.PROFILE_FILL_HINT
                )

            ProfileBottomArea(
                state = bottomState,
                onPrimary = onViewContacts,
                onSecondary = onEditProfile,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@PreviewPhones
@Composable
private fun ProfileScreenPreviewIncomplete() =
    PreviewScreenEdgeToEdge {
        ProfileScreen(
            state = ProfileUiState(
                name = ProfilePreviewText.MyProfile.NAME,
                linePrimary = ProfilePreviewText.MyProfile.CAREER,
                lineSecondary = ProfilePreviewText.MyProfile.ADDRESS,
                isCompleted = false
            ),
            onEditProfile = {}, onViewContacts = {}, onLogout = {}
        )
    }

@PreviewPhones
@Composable
private fun ProfileScreenPreviewCompleted() =
    PreviewScreenEdgeToEdge {
        ProfileScreen(
            state = ProfileUiState(
                name = ProfilePreviewText.MyProfileDetailed.NAME,
                linePrimary = ProfilePreviewText.MyProfileDetailed.CAREER,
                lineSecondary = ProfilePreviewText.MyProfileDetailed.ADDRESS,
                isCompleted = true
            ),
            onEditProfile = {}, onViewContacts = {}, onLogout = {}
        )
    }
