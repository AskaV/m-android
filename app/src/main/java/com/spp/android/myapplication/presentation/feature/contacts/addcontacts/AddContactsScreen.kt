package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.AddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.SearchClicked

@Composable
fun AddContactsScreen(
    onBack: () -> Unit = {},
    onOpenProfile: (Int) -> Unit = {},
    viewModel: AddContactsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NavigateBack -> onBack()
                is ShowMessage -> {
                    effect.message
                }
            }
        }
    }

    BackHandler {
        if (state.isSearchOpen) {
            viewModel.onEvent(Event.SearchClosed)
        } else if (state.isSelectionMode) {
            viewModel.onEvent(Event.ExitSelectionMode)
        } else {
            viewModel.onEvent(BackClicked)
        }
    }

    AddContactsScreenContent(
        items = state.items,
        selectedIds = state.selected,
        onBack = { viewModel.onEvent(BackClicked) },
        onSearchClick = { viewModel.onEvent(SearchClicked) },
        isSearchOpen = state.isSearchOpen,
        onAddClick = { viewModel.onEvent(AddClicked(it)) },
        onItemClick = { c ->
            if (state.isSelectionMode) {
                viewModel.onEvent(Event.UserClicked(c))
            } else {
                onOpenProfile(c.id)
            }
        },
        onItemLongClick = { c ->
            viewModel.onEvent(Event.UserLongClicked(c))
        },
        query = state.query,
        onQueryChange = { viewModel.onEvent(Event.QueryChanged(it)) },
        onSearchClose = { viewModel.onEvent(Event.SearchClosed) },
        onMassAddClick = { viewModel.onEvent(Event.MassAddClicked) }
    )
}
