package com.spp.android.myapplication.presentation.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.spp.android.myapplication.presentation.designsystem.components.buttons.FilledButton
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedBorderButton
import com.spp.android.myapplication.presentation.designsystem.components.buttons.OutlinedButtonStyle
import com.spp.android.myapplication.presentation.designsystem.components.buttons.SocialButtonsRow
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.texts.AppText

data class FilledBtn(
    val text: String,
    val onClick: () -> Unit = {},
) {
    companion object {   //TODO remove if not used
        val Empty = FilledBtn(
            "-1"
        );
    }
}

data class OutlinedBtn(
    val text: String,
    val onClick: () -> Unit = {},
    val style: OutlinedButtonStyle = OutlinedButtonStyle.Secondary
)

@Composable
fun ProfileBottomArea(
    modifier: Modifier = Modifier,
    showSocial: Boolean,
    primaryFilled: FilledBtn?,
    secondaryOutlined: OutlinedBtn?,
    hint: String? = "",
    contentPadding: PaddingValues = PaddingValues(
        horizontal = dimensionResource(R.dimen.spacer_medium),
        vertical = dimensionResource(R.dimen.spacer_medium)
    )
) {
    val betweenButtons = dimensionResource(R.dimen.spacer_medium)
    val topToSocial = dimensionResource(R.dimen.spacer_large)

    Surface(
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.5f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding).navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_additional)))
            Column(
                Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showSocial) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = topToSocial),
                        contentAlignment = Alignment.Center
                    ) { SocialButtonsRow() }
                }

                Spacer(Modifier.weight(1f))

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (!hint.isNullOrBlank()) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = betweenButtons)
                        )
                    }

                    secondaryOutlined?.let { spec ->
                        OutlinedBorderButton(
                            text = spec.text,
                            onClick = spec.onClick,
                            style = spec.style,
                            modifier = Modifier.fillMaxWidth(),
                            buttonHeight = 40.dp
                        )
                        if (primaryFilled != null) Spacer(Modifier.height(betweenButtons))
                    }
                    primaryFilled?.let { spec ->
                        FilledButton(
                            text = spec.text,
                            onClick = spec.onClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@PreviewPhones
@Composable
private fun PreviewProfileBottomMessageOnly() {
    MyApplicationTheme {
        ProfileBottomArea(
            showSocial = true,
            primaryFilled = FilledBtn(
                text = AppText.ContactProfile.MESSAGE_TEXT.text(), onClick = {}),
            secondaryOutlined = null,
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@PreviewPhones
@Composable
private fun PreviewProfileBottomAddAndMessage() {
    MyApplicationTheme {
        ProfileBottomArea(
            showSocial = true,
            primaryFilled = FilledBtn(
                text = AppText.Contacts.ADD.text(), onClick = {}),
            secondaryOutlined = OutlinedBtn(
                text = AppText.ContactProfile.MESSAGE_TEXT.text(), onClick = {}),
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@PreviewPhones
@Composable
private fun PreviewProfileBottomMyProfileCompleted() {
    MyApplicationTheme {
        ProfileBottomArea(
            showSocial = false, primaryFilled = FilledBtn(
                text = AppText.MyProfile.VIEW_CONTACTS.text()
            ), secondaryOutlined = OutlinedBtn(
                text = AppText.EditProfile.TITLE.text()
            ), modifier = Modifier.fillMaxHeight()
        )
    }
}

@PreviewPhones
@Composable
private fun PreviewProfileBottomMyProfileIncomplete() {
    MyApplicationTheme {
        ProfileBottomArea(
            showSocial = false,
            primaryFilled = null,
            secondaryOutlined = OutlinedBtn(
                text = AppText.EditProfile.TITLE.text()
            ),
            hint = AppText.MyProfile.PROFILE_FILL_HINT.text(),
            modifier = Modifier.fillMaxHeight(),
        )
    }
}