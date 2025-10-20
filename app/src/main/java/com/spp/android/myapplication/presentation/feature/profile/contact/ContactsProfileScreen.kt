package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ContactProfileScreen(
    contactId: String,
    onBack: () -> Unit = {},
    onOpenChat: (String) -> Unit = {},
    vm: ContactProfileViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackBar = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(contactId) {
        vm.onEvent(ContactProfileContract.Event.Load(contactId))
    }
    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                ContactProfileContract.Effect.NavigateBack -> onBack()
                is ContactProfileContract.Effect.OpenChat -> onOpenChat(effect.contactId)
                is ContactProfileContract.Effect.ShowMessage -> {
                    snackBar.showSnackbar(effect.messageKey.text(context))
                }
            }
        }
    }

    ContactProfileScreen(
        state = ContactProfileContract.State(
            name = state.name,
            linePrimary = state.linePrimary,
            lineSecondary = state.lineSecondary,
            hasSocial = state.hasSocial
        ),
        onBack = { vm.onEvent(ContactProfileContract.Event.BackClicked) },
        onMessage = { vm.onEvent(ContactProfileContract.Event.MessageClicked) })
}