package com.spp.android.myapplication.presentation.feature.profile.contactsprofile

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
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomArea
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomState
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileHeader
import com.spp.android.myapplication.presentation.designsystem.preview.ProfilePreviewText

data class ContactProfileUiState(
    val name: String,
    val linePrimary: String,
    val lineSecondary: String,
    val hasSocial: Boolean = true
)

@Composable
fun ContactProfileScreen(
    state: ContactProfileUiState,
    onBack: () -> Unit,
    onMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pad = dimensionResource(id = R.dimen.padding_screen)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

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
                    .padding(vertical = spaceL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = pad)
                        .padding(bottom = spaceL / 2)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                    Text(
                        text = ProfilePreviewText.MyProfile.PROFILE,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                ProfileHeader(
                    name = state.name,
                    linePrimary = state.linePrimary,
                    lineSecondary = state.lineSecondary
                )

                Spacer(Modifier.height(spaceL))
            }
        }

        val bottomState: ProfileBottomState =
            if (state.hasSocial) ProfileBottomState.ContactMessageOnly
            else ProfileBottomState.ContactAddable

        ProfileBottomArea(
            state = bottomState,
            onPrimary = onMessage,
            onSecondary = {
                onMessage()
            },
            contentPadding = PaddingValues(horizontal = pad),
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@PreviewPhones
@Composable
private fun ContactProfilePreview() = PreviewScreenEdgeToEdge {
    ContactProfileScreen(
        state = ContactProfileUiState(
            name = ProfilePreviewText.MyProfileDetailed.NAME,
            linePrimary = ProfilePreviewText.MyProfileDetailed.CAREER,
            lineSecondary = ProfilePreviewText.MyProfileDetailed.ADDRESS,
            hasSocial = true
        ),
        onBack = {},
        onMessage = {}
    )
}