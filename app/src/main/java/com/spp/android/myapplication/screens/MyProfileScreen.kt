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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.ui.components.SocialButton
import com.spp.android.myapplication.ui.components.CustomOrangeButton
import com.spp.android.myapplication.ui.components.EditProfileButton

@Preview(
    showBackground = true,
    name = "FigmaMobileSize",
    device = "spec:width=360px,height=720px,dpi=160"
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
                .background(colorResource(id = R.color.blue))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 24.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineLarge,
                        color = colorResource(id = R.color.white)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ProfileAvatar()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.user_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorResource(id = R.color.white)
                )
                Text(
                    text = stringResource(R.string.user_profession),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.grayText2)
                )
                Text(
                    text = stringResource(R.string.user_address),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.grayText2)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f / 3f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top = 0.dp)
            ) {
                SocialButton(R.drawable.social_facebook, stringResource(R.string.facebook))
                SocialButton(R.drawable.social_instagram, stringResource(R.string.instagram))
                SocialButton(R.drawable.social_telegram, stringResource(R.string.telegram))
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
            .size(113.dp)
            .clip(CircleShape)
    )
}

@Composable
fun ActionButtons() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        EditProfileButton(onClick = { /* TODO: Edit Profile Action */ })

        Spacer(modifier = Modifier.height(16.dp))

        CustomOrangeButton(
            text = stringResource(R.string.view_contacts),
            onClick = { /* TODO: View Contacts Action */ }
        )
    }
}
