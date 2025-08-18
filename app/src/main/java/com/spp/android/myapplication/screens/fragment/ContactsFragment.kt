package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.spp.android.myapplication.R
import com.spp.android.myapplication.nav.ContactDetailRoute
import com.spp.android.myapplication.screens.fragment.contact.AddContactDialog
import com.spp.android.myapplication.screens.fragment.contact.Contact
import com.spp.android.myapplication.screens.fragment.contact.ContactItem
import com.spp.android.myapplication.screens.fragment.contact.ContactsViewModel
import com.spp.android.myapplication.screens.fragment.contact.SwipeToDeleteContainer
import com.spp.android.myapplication.screens.fragment.contact.getAvatarTransitionName
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MyApplicationTheme {
                val contacts by viewModel.contacts.collectAsState()
                val selected by viewModel.selected.collectAsState()
                val selectionMode by viewModel.isSelectionMode.collectAsState()

                val context = LocalContext.current

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var showDialog by remember { mutableStateOf(false) }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    floatingActionButton = {
                        if (selectionMode && selected.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    viewModel.deleteSelected()
                                    scope.launch {
                                        val res = snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.deleted_contact_toast_text),
                                            actionLabel = context.getString(R.string.return_contact_toast_text),
                                            duration = SnackbarDuration.Short
                                        )
                                        if (res == SnackbarResult.ActionPerformed) {
                                            viewModel.undoLastBatchDelete()
                                        }
                                    }
                                },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.recycle_bin),
                                    contentDescription = stringResource(R.string.wastebasket_description),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        ContactsTopBar(
                            onBackClick = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                            onSearchClick = { /* TODO */ }
                        )

                        Text(
                            text = stringResource(R.string.add_contacts_text),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(id = R.dimen.spacer_medium),
                                    top = dimensionResource(id = R.dimen.spacer_medium),
                                    bottom = dimensionResource(id = R.dimen.spacer_small)
                                )
                                .clickable { showDialog = true }
                        )


                        ContactList(
                            contacts = contacts,
                            selectionMode = selectionMode,
                            selected = selected,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onNavigateToDetail = { contact, transitionName, avatarView ->
                                val route = ContactDetailRoute(
                                    name = contact.name,
                                    position = contact.position,
                                    avatarUrl = contact.avatarUrl,
                                    transitionName = transitionName
                                )
                                avatarView?.let { view ->
                                    val extras = FragmentNavigatorExtras(view to transitionName)
                                    findNavController().navigate(route, null, extras)
                                } ?: run { findNavController().navigate(route) }
                            },
                            onDeleteContact = { contact ->
                                viewModel.deleteContact(contact)
                            },
                            onUndoSingleDelete = {
                                viewModel.undoDelete()
                            },
                            onToggleSelection = { viewModel.toggleSelection(it) },
                            onStartSelection = { viewModel.startSelection(it) }
                        )
                    }

                    AddContactDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onAddContact = { contact ->
                            viewModel.addContact(contact)
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun ContactsTopBar(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
            .padding(top = dimensionResource(id = R.dimen.spacer_large))
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.surface
            )
        }

        Text(
            text = stringResource(R.string.contacts_text),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search_button),
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun ContactList(
    contacts: List<Contact>,
    selectionMode: Boolean,
    selected: Set<Contact>,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onNavigateToDetail: (Contact, String, ImageView?) -> Unit,
    onDeleteContact: (Contact) -> Unit,
    onUndoSingleDelete: () -> Unit,
    onToggleSelection: (Contact) -> Unit,
    onStartSelection: (Contact) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn {
            items(contacts.size) { index ->
                val contact = contacts[index]
                val avatarTransitionName = contact.getAvatarTransitionName(index)
                val avatarViewState = remember(contact) { mutableStateOf<ImageView?>(null) }
                val context = LocalContext.current

                val onClick = {
                    if (selectionMode) onToggleSelection(contact)
                    else onNavigateToDetail(contact, avatarTransitionName, avatarViewState.value)
                }
                val onLongClick = {
                    if (!selectionMode) onStartSelection(contact)
                    else onToggleSelection(contact)
                }

                if (!selectionMode) {
                    SwipeToDeleteContainer(contact = contact, onDelete = {
                        onDeleteContact(contact)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(R.string.deleted_contact_toast_text),
                                actionLabel = context.getString(R.string.return_contact_toast_text),
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                onUndoSingleDelete()
                            }
                        }
                    }) {
                        ContactItem(
                            contact = contact,
                            avatarTransitionName = avatarTransitionName,
                            onAvatarViewReady = { view -> avatarViewState.value = view },
                            isInSelectionMode = selectionMode,
                            isSelected = selected.contains(contact),
                            onClick = onClick,
                            onLongClick = onLongClick,
                            onDeleteClick = {
                                onDeleteContact(contact)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.deleted_contact_toast_text),
                                        actionLabel = context.getString(R.string.return_contact_toast_text),
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        onUndoSingleDelete()
                                    }
                                }
                            }
                        )
                    }
                } else {
                    ContactItem(
                        contact = contact,
                        avatarTransitionName = avatarTransitionName,
                        onAvatarViewReady = { view -> avatarViewState.value = view },
                        isInSelectionMode = selectionMode,
                        isSelected = selected.contains(contact),
                        onClick = onClick,
                        onLongClick = onLongClick,
                        onDeleteClick = { /* hide */ }
                    )
                }
            }
        }
    }
}
