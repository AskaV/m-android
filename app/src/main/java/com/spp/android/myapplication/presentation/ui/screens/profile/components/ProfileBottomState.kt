package com.spp.android.myapplication.presentation.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons.FilledButton
import com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons.OutlinedButtonStyle

sealed interface ProfileBottomState {
    data class MyProfileIncomplete(val hint: String) : ProfileBottomState
    data object MyProfileCompleted : ProfileBottomState

    data object ContactMessageOnly : ProfileBottomState
    data object ContactAddable : ProfileBottomState
}

@Composable
fun ProfileBottomArea(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f),
    state: ProfileBottomState,
    onPrimary: () -> Unit,
    onSecondary: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is ProfileBottomState.MyProfileCompleted -> {
                    ProfileSocialRowWithSpacer(spaceM)
                    ProfileActionButton(
                        text = ProfilePreviewText.MyProfile.EDIT_PROFILE,
                        onClick = { onSecondary?.invoke() }
                    )
                    Spacer(Modifier.height(spaceM))
                    FilledButton(
                        text = ProfilePreviewText.MyProfile.VIEW_CONTACTS,
                        onClick = onPrimary
                    )
                }

                is ProfileBottomState.MyProfileIncomplete -> {
                    Spacer(Modifier.weight(1f, fill = true))
                    Text(
                        text = state.hint,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(spaceM))
                    ProfileActionButton(
                        text = ProfilePreviewText.MyProfile.EDIT_PROFILE,
                        onClick = { onSecondary?.invoke() }
                    )
                    Spacer(Modifier.height(spaceM))
                    FilledButton(
                        text = ProfilePreviewText.MyProfile.VIEW_CONTACTS,
                        onClick = onPrimary
                    )
                }

                ProfileBottomState.ContactMessageOnly -> {
                    ProfileSocialRowWithSpacer(spaceM)
                    FilledButton(
                        text = ProfilePreviewText.ContactProfile.MESSAGE_TEXT,
                        onClick = onPrimary
                    )
                }

                ProfileBottomState.ContactAddable -> {
                    ProfileSocialRowWithSpacer(spaceM)
                    ProfileActionButton(
                        text = ProfilePreviewText.ContactProfile.MESSAGE_TEXT,
                        onClick = { onSecondary?.invoke() },
                        style = OutlinedButtonStyle.OnBackground
                    )
                    Spacer(Modifier.height(spaceM))
                    ProfileActionButton(
                        text = ProfilePreviewText.MyProfile.EDIT_PROFILE,
                        onClick = { onSecondary?.invoke() }
                    )
                    Spacer(Modifier.height(spaceM))
                    FilledButton(
                        text = ProfilePreviewText.ContactProfile.ADD_TO_MY_CONTACTS,
                        onClick = onPrimary
                    )
                }
            }
            Spacer(Modifier.height(spaceM))
        }
    }
}

@Composable
private fun ProfileActionButton(
    text: String,
    onClick: () -> Unit,
    style: OutlinedButtonStyle = OutlinedButtonStyle.Secondary
) {
    OutlinedBorderButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        text = text,
        onClick = onClick,
        style = style
    )
}


@PreviewPhones
@Composable
private fun PreviewMyProfileCompleted() = PreviewScreenEdgeToEdge {
    ProfileBottomArea(
        state = ProfileBottomState.MyProfileCompleted,
        onPrimary = {},
        onSecondary = {},
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_screen))
    )
}

@PreviewPhones
@Composable
private fun PreviewMyProfileIncomplete() = PreviewScreenEdgeToEdge {
    ProfileBottomArea(
        state = ProfileBottomState.MyProfileIncomplete(
            hint = ProfilePreviewText.MyProfile.PROFILE_FILL_HINT
        ),
        onPrimary = {},
        onSecondary = {},
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_screen))
    )
}

@PreviewPhones
@Composable
private fun PreviewContactMessageOnly() = PreviewScreenEdgeToEdge {
    ProfileBottomArea(
        state = ProfileBottomState.ContactMessageOnly,
        onPrimary = {},
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_screen))
    )
}

@PreviewPhones
@Composable
private fun PreviewContactAddable() = PreviewScreenEdgeToEdge {
    ProfileBottomArea(
        state = ProfileBottomState.ContactAddable,
        onPrimary = {},
        onSecondary = {},
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_screen))
    )
}