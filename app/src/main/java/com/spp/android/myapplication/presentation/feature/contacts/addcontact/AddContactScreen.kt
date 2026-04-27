package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerScreen
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Event

@Composable
fun AddContactScreen(
    onBack: () -> Unit = {},
    viewModel: AddContactViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showPicker by remember { mutableStateOf(false) }
    var pickerKey by remember { mutableIntStateOf(0) }

    if (showPicker) {
        key(pickerKey) {
            GalleryPickerScreen(
                startVisible = true,
                onResult = { uri ->
                    showPicker = false
                    uri?.let { viewModel.onEvent(Event.OnAvatarPicked(it.toString())) }
                },
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateBack -> onBack()
                is Effect.ShowMessage -> {
                    effect.message
                }
            }
        }
    }

    BackHandler { viewModel.onEvent(Event.BackClicked) }

    AddContactScreenContent(
        state = state,
        onBack = { viewModel.onEvent(Event.BackClicked) },
        onAvatarClick = {
            pickerKey++
            showPicker = true
        },
        onSave = { viewModel.onEvent(Event.SaveClicked) },
        onUsernameChange = { viewModel.onEvent(Event.UsernameChanged(it)) },
        onCareerChange = { viewModel.onEvent(Event.CareerChanged(it)) },
        onEmailChange = { viewModel.onEvent(Event.EmailChanged(it)) },
        onPhoneChange = { viewModel.onEvent(Event.PhoneChanged(it)) },
        onAddressChange = { viewModel.onEvent(Event.AddressChanged(it)) },
        onDobChange = { viewModel.onEvent(Event.DateOfBirthChanged(it)) },
    )
}
