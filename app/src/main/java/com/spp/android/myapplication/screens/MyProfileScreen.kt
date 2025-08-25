package com.spp.android.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.SocialButton
import com.spp.android.myapplication.CustomOrangeButton
import com.spp.android.myapplication.EditProfileButton
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.*


@Preview(
    showBackground = true,
    name = "FigmaMobileSize",
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"

)
@Composable
fun MyProfileScreenPreview() {
    MaterialTheme {
        MyProfileScreen()
    }
}


@Composable
fun MyProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Blue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.default_padding)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.default_padding),
                            top = dimensionResource(id = R.dimen.margin_top_settings)
                        ),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineLarge,
                        color = White
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.default_spacing)))

                ProfileAvatar()

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_spacing)))

                Text(
                    text = stringResource(R.string.user_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = White
                )
                Text(
                    text = stringResource(R.string.user_profession),
                    style = MaterialTheme.typography.titleMedium,
                    color = GrayText2
                )
                Text(
                    text = stringResource(R.string.user_address),
                    style = MaterialTheme.typography.titleMedium,
                    color = GrayText2
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f / 3f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.social_button_spacing)),
                modifier = Modifier.padding(top = 0.dp)
            ) {
                SocialButton(
                    iconRes = R.drawable.social_facebook,
                    contentDescription = stringResource(R.string.facebook)
                )

                SocialButton(
                    iconRes = R.drawable.social_instagram,
                    contentDescription = stringResource(R.string.instagram)
                )

                SocialButton(
                    iconRes = R.drawable.social_telegram,
                    contentDescription = stringResource(R.string.telegram)
                )
            }

            Spacer(modifier = Modifier.weight(2f / 3f))

            ActionButtons()
        }
    }
}

@Composable
fun ProfileAvatar() {
    Image(
        painter = painterResource(id = R.drawable.baseline_account_circle_avatar),
        contentDescription = stringResource(R.string.user_avatar),
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.profile_avatar_size))
            .clip(CircleShape)
    )
}

@Composable
fun ActionButtons() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.default_padding))
            .padding(bottom = dimensionResource(id = R.dimen.default_padding))
    ) {
        EditProfileButton(onClick = { /* TODO: Edit Profile Action */ })

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.default_spacing)))

        CustomOrangeButton(
            text = stringResource(R.string.view_contacts).uppercase(),
            onClick = { /* TODO: View Contacts Action */ }
        )
    }
}