package com.spp.android.myapplication.presentation.feature.contacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ContactsScreen(
    onBack: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenAddContacts: () -> Unit,
    onOpenContactProfile: (String) -> Unit,
    vm: ContactsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                ContactsContract.Effect.NavigateBack -> onBack()
                ContactsContract.Effect.OpenSearch -> onOpenSearch()
                ContactsContract.Effect.OpenAddContacts -> onOpenAddContacts()
                is ContactsContract.Effect.OpenContactProfile -> onOpenContactProfile(eff.contactId)
                is ContactsContract.Effect.ShowMessage -> { /* TODO Snackbar */ }
            }
        }
    }

    ContactsScreenContent(
        items = state.items,
        onBack = { vm.onEvent(ContactsContract.Event.BackClicked) },
        onSearchClick = { vm.onEvent(ContactsContract.Event.SearchClicked) },
        onAddContactsClick = { vm.onEvent(ContactsContract.Event.AddContactsClicked) },
        onContactClick = { vm.onEvent(ContactsContract.Event.ContactClicked(it)) },
        onDeleteClick = { vm.onEvent(ContactsContract.Event.DeleteClicked(it)) }
    )
}