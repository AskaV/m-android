package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.activity.compose.BackHandler
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
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenAddContact
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenContactProfile
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.OpenSearch
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event
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
import com.spp.android.myapplication.presentation.notifications.ContactsNotificationHelper
import com.spp.android.myapplication.presentation.notifications.EnsureNotificationsPermission
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenAddContact: () -> Unit = {},
    onOpenAddContacts: () -> Unit = {},
    onOpenContactProfile: (Int) -> Unit = {},
    viewModel: ContactsViewModel = hiltViewModel(),
) {
    val undo = rememberUndoSnackbarController(totalSeconds = 5)
    val snackbar = undo.hostState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    EnsureNotificationsPermission()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
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
                            onUndo = { viewModel.onEvent(UndoDelete) },
                            onTimeout = { },
                        )
                    } else {
                        scope.launch { snackbar.showSnackbar(msg) }
                    }
                }
                is ContactsContract.Effect.ShowDeletedNotification -> {
                    //android.util.Log.d("ContactsUI", "Show notif for id=${effect.contactId}")

                    ContactsNotificationHelper.showDeleted(
                        context = context,
                        contactId = effect.contactId,

                    )
                }
            }
        }
    }
    BackHandler {
        if (state.isSearchOpen) {
            viewModel.onEvent(Event.SearchClosed)
        } else if (state.isSelectionMode) {
            viewModel.onEvent(ExitSelectionMode)
        } else {
            viewModel.onEvent(BackClicked)
        }
    }
    Scaffold(
        snackbarHost = { undo.Host() },
    ) { paddingValues ->
        ContactsScreenContent(
            modifier = Modifier,
            items = state.items,
            onBack = {
                if (state.isSelectionMode) {
                    viewModel.onEvent(ExitSelectionMode)
                } else {
                    viewModel.onEvent(BackClicked)
                }
            },
            onAddContactClick = { viewModel.onEvent(AddContactsClicked) },
            onAddContactsClick = { onOpenAddContacts() },
            onContactClick = {
                if (state.isSelectionMode) {
                    viewModel.onEvent(
                        ContactSelectionToggled(
                            it,
                        ),
                    )
                } else {
                    viewModel.onEvent(ContactClicked(it))
                }
            },
            onDeleteClick = { viewModel.onEvent(DeleteClicked(it)) },
            onContactLongClick = { viewModel.onEvent(ContactLongClicked(it)) },
            showRecycleBin = state.isSelectionMode,
            onBulkDeleteClick = { viewModel.onEvent(BulkDeleteClicked) },
            isSelectionMode = state.isSelectionMode,
            selectedIds = state.selected,
            isSearchOpen = state.isSearchOpen,
            query = state.query,
            onQueryChange = { viewModel.onEvent(Event.QueryChanged(it)) },
            onSearchClose = { viewModel.onEvent(Event.SearchClosed) },
            onSearchClick = { viewModel.onEvent(SearchClicked) },
        )
    }
}
