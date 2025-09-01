package com.spp.android.myapplication.ui.screens.fragment.contact

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.toRoute
import com.spp.android.myapplication.ui.components.FilledButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.ui.components.SocialButton
import com.spp.android.myapplication.ui.nav.ContactDetailRoute
import com.spp.android.myapplication.ui.screens.util.extensions.LoadWithGlide
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

//todo separate package and Composable
class ContactDetailFragment : Fragment() {

    private lateinit var route: ContactDetailRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        postponeEnterTransition()

        route = findNavController()
            .getBackStackEntry<ContactDetailRoute>()
            .toRoute()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MyApplicationTheme {
                val name = route.name
                val position = route.position
                val avatarUrl = route.avatarUrl
                val transitionName = route.transitionName

                Column(modifier = Modifier.fillMaxSize()) {

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
                                    .padding(top = dimensionResource(id = R.dimen.margin_top_settings)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_back),
                                        contentDescription = "Back",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = stringResource(R.string.profile_text),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.size(24.dp))
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

//                            LoadAvatarComposable(
//                                url = avatarUrl,
//                                modifier = Modifier
//                                    .size(dimensionResource(id = R.dimen.profile_avatar_size))
//                                    .clip(CircleShape)
//                            )

                            LoadWithGlide(
                                url = avatarUrl,
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.profile_avatar_size))
                                    .clip(CircleShape),
                                transitionName = transitionName
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_small)))

                            Text(
                                text = name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = position,
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
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensionResource(id = R.dimen.social_button_spacing)
                            )
                        ) {
                            SocialButton(
                                R.drawable.social_facebook,
                                stringResource(R.string.facebook)
                            )
                            SocialButton(
                                R.drawable.social_instagram,
                                stringResource(R.string.instagram)
                            )
                            SocialButton(
                                R.drawable.social_telegram,
                                stringResource(R.string.telegram)
                            )
                        }

                        Spacer(modifier = Modifier.weight(2f / 3f))

                        FilledButton(
                            text = stringResource(R.string.message_text).uppercase(),
                            onClick = {},
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
                                .padding(bottom = dimensionResource(id = R.dimen.spacer_medium))
                        )
                    }
                }
            }
        }
    }
}