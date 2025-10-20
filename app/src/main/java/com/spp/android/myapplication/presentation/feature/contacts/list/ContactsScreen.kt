package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenAddContact: () -> Unit = {},
    onOpenContactProfile: (Int) -> Unit = {},
    vm: ContactsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                ContactsContract.Effect.NavigateBack -> onBack()
                ContactsContract.Effect.OpenSearch -> onOpenSearch()
                ContactsContract.Effect.OpenAddContact -> onOpenAddContact()
                is ContactsContract.Effect.OpenContactProfile -> onOpenContactProfile(eff.contactId)
                is ContactsContract.Effect.ShowMessage -> {
                    scope.launch {
                        snackbar.showSnackbar(eff.messageKey.text(context))
                    }                }
            }
        }
    }

    ContactsScreenContent(
        items = state.items,
        onBack = {
            if (state.isSelectionMode) {
                vm.onEvent(ContactsContract.Event.ExitSelectionMode)
            } else {
                vm.onEvent(ContactsContract.Event.BackClicked)
            }

        },
        onSearchClick = { vm.onEvent(ContactsContract.Event.SearchClicked) },
        onAddContactsClick = { vm.onEvent(ContactsContract.Event.AddContactsClicked) },
        onContactClick = {
            if (state.isSelectionMode) vm.onEvent(ContactsContract.Event.ContactSelectionToggled(it))
            else vm.onEvent(ContactsContract.Event.ContactClicked(it))
        },
        onDeleteClick = { vm.onEvent(ContactsContract.Event.DeleteClicked(it)) },
        onContactLongClick = { vm.onEvent(ContactsContract.Event.ContactLongClicked(it)) },
        showRecycleBin = state.isSelectionMode,
        onBulkDeleteClick = { vm.onEvent(ContactsContract.Event.BulkDeleteClicked) },
        isSelectionMode = state.isSelectionMode,
        selectedIds = state.selected,
    )
}