package com.spp.android.myapplication.ui.screens.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.ui.components.FilledButton
import com.spp.android.myapplication.ui.components.OutlinedBorderButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.ui.components.SocialButton
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.ui.screens.util.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        //todo to Navigtor
        setContent {
            val themePref = remember {
                requireContext()
                    .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    .getString("theme_pref", "system") ?: "system"
            }

            //todo move to MyProfileScreen
            var displayName by remember { mutableStateOf("User") }
            LaunchedEffect(Unit) {
                val email =
                    withContext(Dispatchers.IO) { UserPreferences.getEmail(requireContext()) }
                displayName = email?.let(::parseNameFromEmail) ?: "User"
            }

            MyApplicationTheme(themePref = themePref) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                ) {
                    MyProfileScreen(userName = displayName)
                }
            }
        }
    }
}

//todo separate file
@Preview(
    showBackground = true,
    name = "FigmaMobileSize",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO,  //UI_MODE_NIGHT_YES,
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"

)
@Composable
fun MyProfileScreenPreview() {
    MyApplicationTheme(themePref = "system") { // colored or system
        MyProfileScreen(userName = stringResource(R.string.user_name))
    }
}

//todo viewmodel
fun parseNameFromEmail(email: String): String {
    return email.substringBefore("@")
        .split(".", "_", "-")
        .joinToString(" ") { part -> part.replaceFirstChar { it.uppercase() } }
}

@Composable
fun MyProfileScreen(userName: String, modifier: Modifier = Modifier) {
    //todo MVVM

    //todo Logout button
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.spacer_medium),
                            top = dimensionResource(id = R.dimen.margin_top_settings)
                        ),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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

//todo separate file + preview
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

//todo separate file + preview
@Composable
fun ActionButtons() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
            .padding(bottom = dimensionResource(id = R.dimen.spacer_medium))
    ) {
        OutlinedBorderButton(
            text = stringResource(R.string.edit_profile),
            onClick = { /* TODO: Edit Profile Action */ }, //todo empty
            borderColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

        FilledButton(
            text = stringResource(R.string.view_contacts).uppercase(),
            onClick = { /* TODO: View Contacts Action */ },  //todo empty
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}