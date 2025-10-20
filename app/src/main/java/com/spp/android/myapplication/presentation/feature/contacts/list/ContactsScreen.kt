package com.spp.android.myapplication.presentation.feature.contacts.list

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.components.rememberUndoSnackbarController
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenAddContact: () -> Unit = {},
    onOpenContactProfile: (Int) -> Unit = {},
    vm: ContactsViewModel = hiltViewModel()
) {
    val undo = rememberUndoSnackbarController(totalSeconds = 5)
    val snackbar = undo.hostState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                ContactsContract.Effect.NavigateBack -> onBack()
                ContactsContract.Effect.OpenSearch -> onOpenSearch()
                ContactsContract.Effect.OpenAddContact -> onOpenAddContact()
                is ContactsContract.Effect.OpenContactProfile -> onOpenContactProfile(effect.contactId)

                is ContactsContract.Effect.ShowMessage -> {
                    val msg = effect.messageKey.text(context)
                    val isRemoval = effect.messageKey == AppText.OtherInfo.CONTACTS_REMOVED

                    if (isRemoval) {
                        undo.showUndo(
                            scope = scope,
                            message = msg,
                            undoLabel = "Undo",
                            onUndo = { vm.onEvent(ContactsContract.Event.UndoDelete) },
                            onTimeout = { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() })
                    } else {
                        scope.launch { snackbar.showSnackbar(msg) }
                    }
                }

            }
        }
    }

    Scaffold(
        snackbarHost = { undo.Host() }) { paddingValues ->
        ContactsScreenContent(
            modifier = Modifier.padding(paddingValues),
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
                if (state.isSelectionMode) vm.onEvent(
                    ContactsContract.Event.ContactSelectionToggled(
                        it
                    )
                )
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
}