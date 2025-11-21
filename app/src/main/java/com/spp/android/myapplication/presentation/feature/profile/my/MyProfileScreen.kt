package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.spp.android.myapplication.presentation.feature.profile.edit.ProfileResult
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToAuth
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToContacts
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToEditProfile
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.EditProfileClicked
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.LogoutClicked
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.ViewContactsClicked
import com.spp.android.myapplication.presentation.texts.AppText

object Keys {
    const val PROFILE_RESULT = "profile_result"
}

@Composable
fun MyProfileScreen(
    externalEmail: String? = null,
    externalName: String? = null,
    onNavigateContacts: () -> Unit = {},
    onNavigateEdit: () -> Unit,
    onNavigateAuth: () -> Unit,
    navController: NavController,
    viewModel: MyProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackBar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ShowMessage -> snackBar.showSnackbar(effect.messageKey.text(context))
                is NavigateToContacts -> onNavigateContacts()
                is NavigateToEditProfile -> onNavigateEdit()
                is NavigateToAuth -> onNavigateAuth()
            }
        }
    }
    LaunchedEffect(externalName) {
        if (!externalName.isNullOrBlank()) {
            viewModel.onExternalName(externalName)
        }
    }
    LaunchedEffect(externalEmail) {
        if (!externalEmail.isNullOrBlank()) {
            viewModel.onExternalEmail(externalEmail)
        }
    }
    val navBackStackEntry = navController.currentBackStackEntry

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<ProfileResult?>(Keys.PROFILE_RESULT, null)
            ?.collect { result ->
                if (result != null) {
                    viewModel.onEvent(
                        MyProfileContract.Event.ProfileSaved(
                            username = result.username,
                            career = result.career,
                            phone = result.phone,
                            address = result.address,
                            birthdate = result.birthdate,
                        ),
                    )
                    navBackStackEntry.savedStateHandle[Keys.PROFILE_RESULT] = null
                }
            }
    }

    val careerLabel = AppText.EditProfile.CAREER_LABEL.text()
    val addressLabel = AppText.EditProfile.ADDRESS_LABEL.text()

    val displayPrimary =
        if (state.isCompleted && state.linePrimary.isNotBlank()) {
            state.linePrimary
        } else {
            careerLabel
        }
    val displaySecondary =
        if (state.isCompleted && state.lineSecondary.isNotBlank()) {
            state.lineSecondary
        } else {
            addressLabel
        }

    ProfileScreen(
        state =
            state.copy(
                linePrimary = displayPrimary,
                lineSecondary = displaySecondary,
            ),
        onEditProfile = { viewModel.onEvent(EditProfileClicked) },
        onViewContacts = { viewModel.onEvent(ViewContactsClicked) },
        onLogout = { viewModel.onEvent(LogoutClicked) },
    )
}
