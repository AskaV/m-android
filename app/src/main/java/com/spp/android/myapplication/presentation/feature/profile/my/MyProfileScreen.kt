package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract as C

@Composable
fun MyProfileScreen(
    onNavigateContacts: () -> Unit = {},
    onNavigateEdit: () -> Unit,
    onNavigateAuth: () -> Unit,
    vm: MyProfileViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                is C.Effect.ShowMessage -> { /* TODO: Snackbar */
                }

                C.Effect.NavigateToContacts -> onNavigateContacts()
                C.Effect.NavigateToEditProfile -> onNavigateEdit()
                C.Effect.NavigateToAuth -> onNavigateAuth()
            }
        }
    }

    ProfileScreen(
        state =  MyProfileContract.State(
        name = state.name,
        linePrimary = state.linePrimary,
        lineSecondary = state.lineSecondary,
        isCompleted = state.isCompleted
    ),
        onEditProfile = { vm.onEvent(C.Event.EditProfileClicked) },
        onViewContacts = { vm.onEvent(C.Event.ViewContactsClicked) },
        onLogout = { vm.onEvent(C.Event.LogoutClicked) })
}