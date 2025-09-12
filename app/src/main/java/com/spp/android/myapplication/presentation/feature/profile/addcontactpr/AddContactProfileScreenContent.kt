package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomArea
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomState
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileHeader

@Composable
fun AddContactProfileScreenContent(
    state: AddContactProfileContract.State,
    onBack: () -> Unit,
    onMessage: () -> Unit,
    onAddToContacts: () -> Unit
) {
    val pad = dimensionResource(R.dimen.spacer_medium)
    Column(
        modifier = Modifier
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
                        .padding(horizontal = pad)
                        .padding(bottom = dimensionResource(id = R.dimen.spacer_large))
                ) {
                    androidx.compose.material3.IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                ProfileHeader(
                    name = state.name,
                    linePrimary = state.linePrimary,
                    lineSecondary = state.lineSecondary
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_large)))
            }
        }

        val bottomState =
            if (state.isInMyContacts)
                ProfileBottomState.ContactMessageOnly
            else
                ProfileBottomState.ContactAddable
        ProfileBottomArea(
            state = bottomState,
            onPrimary = { if (state.isInMyContacts) onMessage() else onAddToContacts() },
            onSecondary = { if (!state.isInMyContacts) onMessage() },
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = pad)
        )
    }
}

@PreviewPhones
@Composable
private fun AddContactProfilePreviewDefault() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AddContactProfileScreenContent(
                state = AddContactProfileContract.State(
                    id = "u1",
                    name = "Jenny Walker",
                    linePrimary = "Make-up artist",
                    lineSecondary = "775 Westminster Ave APT D5\nBrooklyn, NY, 11230",
                    isInMyContacts = false
                ),
                onBack = {},
                onMessage = {},
                onAddToContacts = {}
            )
        }
    }
}

@PreviewPhones
@Composable
private fun AddContactProfilePreviewInMyContacts() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AddContactProfileScreenContent(
                state = AddContactProfileContract.State(
                    id = "u2",
                    name = "Lucile Alvarado",
                    linePrimary = "Graphic designer",
                    lineSecondary = "5295 Gaylord Walks Apk. 110",
                    isInMyContacts = true
                ),
                onBack = {},
                onMessage = {},
                onAddToContacts = {}
            )
        }
    }
}