package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    contactId: Int,
    onBack: () -> Unit = {},
    viewModel: AddContactProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(contactId) { viewModel.onEvent(Load(contactId)) }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ShowMessage -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
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
