package com.spp.android.myapplication.presentation.feature.contacts.list

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.components.rememberUndoSnackbarController
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenAddContact
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenContactProfile
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenSearch
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.AddContactsClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.BulkDeleteClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactLongClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactSelectionToggled
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.DeleteClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ExitSelectionMode
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.SearchClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.UndoDelete
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenAddContact: () -> Unit = {},
    onOpenContactProfile: (Int) -> Unit = {},
    vm: ContactsViewModel = hiltViewModel(),
) {
    val undo = rememberUndoSnackbarController(totalSeconds = 5)
    val snackbar = undo.hostState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is NavigateBack -> onBack()
                is OpenSearch -> onOpenSearch()
                is OpenAddContact -> onOpenAddContact()
                is OpenContactProfile -> onOpenContactProfile(effect.contactId)

                is ShowMessage -> {
                    val msg = effect.messageKey.text(context)
                    val isRemoval = effect.messageKey == AppText.OtherInfo.CONTACTS_REMOVED

                    if (isRemoval) {
                        undo.showUndo(
                            scope = scope,
                            message = msg,
                            undoLabel = "Undo",
                            onUndo = { vm.onEvent(UndoDelete) },
                            onTimeout = { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() },
                        )
                    } else {
                        scope.launch { snackbar.showSnackbar(msg) }
                    }
                }
            }
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.onEvent(ContactsContract.Event.Load)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scaffold(
        snackbarHost = { undo.Host() },
    ) { paddingValues ->
        ContactsScreenContent(
            modifier = Modifier,
            items = state.items,
            onBack = {
                if (state.isSelectionMode) {
                    vm.onEvent(ExitSelectionMode)
                } else {
                    vm.onEvent(BackClicked)
                }
            },
            onSearchClick = { vm.onEvent(SearchClicked) },
            onAddContactsClick = { vm.onEvent(AddContactsClicked) },
            onContactClick = {
                if (state.isSelectionMode) {
                    vm.onEvent(
                        ContactSelectionToggled(
                            it,
                        ),
                    )
                } else {
                    vm.onEvent(ContactClicked(it))
                }
            },
            onDeleteClick = { vm.onEvent(DeleteClicked(it)) },
            onContactLongClick = { vm.onEvent(ContactLongClicked(it)) },
            showRecycleBin = state.isSelectionMode,
            onBulkDeleteClick = { vm.onEvent(BulkDeleteClicked) },
            isSelectionMode = state.isSelectionMode,
            selectedIds = state.selected,
        )
    }
}
