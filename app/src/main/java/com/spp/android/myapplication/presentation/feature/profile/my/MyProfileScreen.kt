package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is MyProfileContract.Effect.ShowMessage -> { /* TODO: Snackbar */ }
                MyProfileContract.Effect.NavigateToContacts -> onNavigateContacts()
                MyProfileContract.Effect.NavigateToEditProfile -> onNavigateEdit()
                MyProfileContract.Effect.NavigateToAuth -> onNavigateAuth()
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
    // Pass through the state as-is (UI is dumb)
    ProfileScreen(
        state = state,
        onEditProfile = { viewModel.onEvent(MyProfileContract.Event.EditProfileClicked) },
        onViewContacts = { viewModel.onEvent(MyProfileContract.Event.ViewContactsClicked) },
        onLogout = { viewModel.onEvent(MyProfileContract.Event.LogoutClicked) })
}