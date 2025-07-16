package com.spp.android.myapplication.screens.contacts

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.FilledButton
import com.spp.android.myapplication.OutlinedBorderButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.SocialButton
import com.spp.android.myapplication.screens.parseNameFromEmail
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

class ContactProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("theme_pref", "system") ?: "system"

        val email = intent.getStringExtra("email")
        val displayName = email?.let { parseNameFromEmail(it) } ?: "User"

        setContent {
            MyApplicationTheme(themePref = themePref) {
                ContactsProfileScreen(displayName)
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "FigmaMobileSize",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO,  //UI_MODE_NIGHT_YES,
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"

)
@Composable
fun ContactsProfileScreenPreview() {
    MyApplicationTheme(themePref = "system") { // colored or system
        ContactsProfileScreen(userName = stringResource(R.string.user_name))
    }
}

fun parseNameFromEmail(email: String): String {
    return email.substringBefore("@")
        .split(".", "_", "-")
        .joinToString(" ") { part -> part.replaceFirstChar { it.uppercase() } }
}

@Composable
fun ContactsProfileScreen(userName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.spacer_medium),
                            start = dimensionResource(id = R.dimen.spacer_medium),
                            end = dimensionResource(id = R.dimen.spacer_medium)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* TODO: Handle back */ },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.profile_text),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large)))
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

                ProfileAvatar()

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_small)))

                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.user_profession),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.user_address),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
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
        painter = painterResource(id = R.drawable.profile_avatar),
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
            .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
            .padding(bottom = dimensionResource(id = R.dimen.spacer_medium))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

        FilledButton(
            text = stringResource(R.string.message_text).uppercase(),
            onClick = { /* TODO: View Contacts Action */ },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}