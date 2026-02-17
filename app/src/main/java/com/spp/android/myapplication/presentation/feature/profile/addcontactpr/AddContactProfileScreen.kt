package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.AddToContactsClicked
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.Load
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.MessageClicked

@Composable
fun AddContactProfileScreen(
    contactId: Int = 0,
    onBack: () -> Unit = {},
    viewModel: AddContactProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contactId) { viewModel.onEvent(Load(contactId)) }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ShowMessage -> {
                    effect.message
                }

                is NavigateBack -> onBack()
            }
        }
    }

    AddContactProfileScreenContent(
        state = state,
        onBack = { viewModel.onEvent(BackClicked) },
        onMessage = { viewModel.onEvent(MessageClicked) },
        onAddToContacts = { viewModel.onEvent(AddToContactsClicked) },
    )
}
