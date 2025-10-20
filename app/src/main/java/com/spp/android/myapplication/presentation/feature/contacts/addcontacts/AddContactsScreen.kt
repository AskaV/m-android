package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenProfile: (Int) -> Unit = {},
    vm: AddContactsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                AddContactsContract.Effect.NavigateBack -> onBack()
                AddContactsContract.Effect.OpenSearch -> onOpenSearch()
                is AddContactsContract.Effect.ShowMessage -> { /* TODO() */
                }
            }
        }
    }

    BackHandler { vm.onEvent(AddContactsContract.Event.BackClicked) }

    AddContactsScreenContent(
        items = state.items,
        selectedIds = state.selected,
        onBack = { vm.onEvent(AddContactsContract.Event.BackClicked) },
        onSearchClick = { vm.onEvent(AddContactsContract.Event.SearchClicked) },
        onMassAddClick = { vm.onEvent(AddContactsContract.Event.MassAddClicked) },
        onAddClick = { vm.onEvent(AddContactsContract.Event.AddClicked(it)) },
        onRowClick = { contact -> onOpenProfile(contact.id) })
}