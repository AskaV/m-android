package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.FilledButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.SocialButton
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

class ContactDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MyApplicationTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Верх
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.spacer_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Back"
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = stringResource(R.string.profile_text),
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.profile_avatar_size))
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Mock Name",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = stringResource(R.string.user_profession),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = stringResource(R.string.user_address),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.weight(1f / 3f))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.social_button_spacing)),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        SocialButton(R.drawable.social_facebook, stringResource(R.string.facebook))
                        SocialButton(
                            R.drawable.social_instagram,
                            stringResource(R.string.instagram)
                        )
                        SocialButton(R.drawable.social_telegram, stringResource(R.string.telegram))
                    }

                    Spacer(modifier = Modifier.weight(2f / 3f))

                    FilledButton(
                        text = stringResource(R.string.message_text).uppercase(),
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}