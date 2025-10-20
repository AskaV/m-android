package com.spp.android.myapplication.presentation.feature.profile.contact

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
import com.spp.android.myapplication.presentation.feature.profile.components.FilledBtn
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileBottomArea
import com.spp.android.myapplication.presentation.feature.profile.components.ProfileHeader
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun ContactProfileScreen(
    modifier: Modifier = Modifier,
    state: ContactProfileContract.State,
    onBack: () -> Unit = {},
    onMessage: () -> Unit = {}
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = spaceL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.fillMaxWidth().padding(horizontal = pad).padding(bottom = spaceL / 2)
                ) {
                    IconButton(
                        onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                    Text(
                        text = AppText.MyProfile.PROFILE.text(),
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

        ProfileBottomArea(
            showSocial = state.hasSocial,
            primaryFilled = FilledBtn(
                AppText.ContactProfile.MESSAGE_TEXT.text(), onClick = onMessage
            ),
            secondaryOutlined = null,
            contentPadding = PaddingValues(horizontal = pad, vertical = pad),
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@PreviewPhones
@Composable
private fun ContactProfilePreview() = PreviewScreenEdgeToEdge {
    ContactProfileScreen(
        state = ContactProfileContract.State(
            name = AppText.MyProfileDetailed.NAME.text(),
            linePrimary = AppText.MyProfileDetailed.CAREER.text(),
            lineSecondary = AppText.MyProfileDetailed.ADDRESS.text()
        ),
    )
}