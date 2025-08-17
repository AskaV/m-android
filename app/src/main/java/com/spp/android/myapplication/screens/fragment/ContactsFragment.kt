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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
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
import com.spp.android.myapplication.screens.fragment.contact.ContactItem
import com.spp.android.myapplication.screens.fragment.contact.ContactsViewModel
import com.spp.android.myapplication.screens.fragment.contact.SwipeToDeleteContainer
import com.spp.android.myapplication.screens.fragment.contact.getAvatarTransitionName
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
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
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var showDialog by remember { mutableStateOf(false) }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))
                                .padding(top = dimensionResource(id = R.dimen.spacer_large))
                        ) {
                            IconButton(
                                onClick = { /* back */ },
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
                                onClick = { /* search */ },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_button),
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        }

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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            LazyColumn {
                                items(contacts.size) { index ->
                                    val contact = contacts[index]
                                    val message =
                                        stringResource(R.string.deleted_contact_toast_text)
                                    val actionLabel =
                                        stringResource(R.string.return_contact_toast_text)
                                    val avatarTransitionName =
                                        contact.getAvatarTransitionName(index)
                                    val avatarViewState =
                                        remember(contact) { mutableStateOf<ImageView?>(null) }


                                    SwipeToDeleteContainer(contact = contact, onDelete = {
                                        viewModel.deleteContact(contact)
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = message,
                                                actionLabel = actionLabel,
                                                duration = SnackbarDuration.Short
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                viewModel.undoDelete()
                                            }
                                        }
                                    }) {
                                        ContactItem(
                                            contact = contact,
                                            avatarTransitionName = avatarTransitionName,
                                            onAvatarViewReady = { view ->
                                                avatarViewState.value = view
                                            },

                                            onClick = {
                                                val route = ContactDetailRoute(
                                                    name = contact.name,
                                                    position = contact.position,
                                                    avatarUrl = contact.avatarUrl,
                                                    transitionName = avatarTransitionName
                                                )

                                                avatarViewState.value?.let { view ->
                                                    val extras =
                                                        FragmentNavigatorExtras(view to avatarTransitionName)
                                                    findNavController().navigate(
                                                        route,
                                                        null,
                                                        extras
                                                    )
                                                } ?: run {
                                                    findNavController().navigate(route)
                                                }
                                            },
                                            onDeleteClick = {
                                                viewModel.deleteContact(contact)
                                                scope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        message = message,
                                                        actionLabel = actionLabel,
                                                        duration = SnackbarDuration.Short
                                                    )
                                                    if (result == SnackbarResult.ActionPerformed) {
                                                        viewModel.undoDelete()
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
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