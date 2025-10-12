package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddContactProfileScreen(
    contactId: String, onBack: () -> Unit = {}, vm: AddContactProfileViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(contactId) { vm.onEvent(AddContactProfileContract.Event.Load(contactId)) }
    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                is AddContactProfileContract.Effect.ShowMessage -> { /* TODO */
                }

                AddContactProfileContract.Effect.NavigateBack -> onBack()
            }
        }
    }

    AddContactProfileScreenContent(
        state = state,
        onBack = { vm.onEvent(AddContactProfileContract.Event.BackClicked) },
        onMessage = { vm.onEvent(AddContactProfileContract.Event.MessageClicked) },
        onAddToContacts = { vm.onEvent(AddContactProfileContract.Event.AddToContactsClicked) })
}