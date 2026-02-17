package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect.OpenSearch
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.AddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.MassAddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.SearchClicked

@Composable
fun AddContactsScreen(
    onBack: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenProfile: (Int) -> Unit = {},
    viewModel: AddContactsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NavigateBack -> onBack()
                is OpenSearch -> onOpenSearch()
                is ShowMessage -> {
                    effect.message
                }
            }
        }
    }

    BackHandler { viewModel.onEvent(BackClicked) }

    AddContactsScreenContent(
        items = state.items,
        selectedIds = state.selected,
        onBack = { viewModel.onEvent(BackClicked) },
        onSearchClick = { viewModel.onEvent(SearchClicked) },
        onMassAddClick = { viewModel.onEvent(MassAddClicked) },
        onAddClick = { viewModel.onEvent(AddClicked(it)) },
        onRowClick = { contact -> onOpenProfile(contact.id) },
    )
}
