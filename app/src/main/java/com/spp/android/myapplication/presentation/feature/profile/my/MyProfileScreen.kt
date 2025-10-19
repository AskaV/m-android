package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToAuth
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToContacts
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.NavigateToEditProfile
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect.ShowMessage

@Composable
fun MyProfileScreen(
    externalEmail: String? = null,
    externalName: String? = null,
    onNavigateContacts: () -> Unit = {},
    onNavigateEdit: () -> Unit,
    onNavigateAuth: () -> Unit,
    viewModel: MyProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is ShowMessage -> snackbar.showSnackbar(eff.messageKey.text(context))
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
    ProfileScreen(
        state = state,
        onEditProfile = { viewModel.onEvent(MyProfileContract.Event.EditProfileClicked) },
        onViewContacts = { viewModel.onEvent(MyProfileContract.Event.ViewContactsClicked) },
        onLogout = { viewModel.onEvent(MyProfileContract.Event.LogoutClicked) })
}