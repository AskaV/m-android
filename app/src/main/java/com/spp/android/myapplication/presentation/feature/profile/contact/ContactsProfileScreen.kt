package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Effect.OpenChat
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Event.Load
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Event.MessageClicked

@Composable
fun ContactProfileScreen(
    contactId: Int,
    onBack: () -> Unit = {},
    onOpenChat: (Int) -> Unit = {},
    viewModel: ContactProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(contactId) {
        viewModel.onEvent(Load(contactId))
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NavigateBack -> onBack()
                is OpenChat -> onOpenChat(effect.contactId)
                is ShowMessage -> {
                    snackBar.showSnackbar(effect.messageKey.text(context))
                }
            }
        }
    }

    ContactProfileScreen(
        state = state,
        onBack = { viewModel.onEvent(BackClicked) },
        onMessage = { viewModel.onEvent(MessageClicked) },
    )
}
