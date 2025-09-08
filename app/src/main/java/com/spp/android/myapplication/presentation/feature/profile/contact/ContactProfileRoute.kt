package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ContactProfileRoute(
    contactId: String,
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    vm: ContactProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(contactId) {
        vm.onEvent(ContactProfileContract.Event.Load(contactId))
    }
    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                ContactProfileContract.Effect.NavigateBack -> onBack()
                is ContactProfileContract.Effect.OpenChat -> onOpenChat(eff.contactId)
                is ContactProfileContract.Effect.ShowMessage -> { /* TODO: Snackbar */ }
            }
        }
    }

    ContactProfileScreen(
        state = ContactProfileUiState(
            name = state.name,
            linePrimary = state.linePrimary,
            lineSecondary = state.lineSecondary,
            hasSocial = state.hasSocial
        ),
        onBack = { vm.onEvent(ContactProfileContract.Event.BackClicked) },
        onMessage = { vm.onEvent(ContactProfileContract.Event.MessageClicked) }
    )
}