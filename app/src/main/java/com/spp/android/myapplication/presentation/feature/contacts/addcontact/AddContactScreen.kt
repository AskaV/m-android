package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    vm: AddContactViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    var showPicker by remember { mutableStateOf(false) }
    var pickerKey by remember { mutableIntStateOf(0) }

    if (showPicker) {
        key(pickerKey) {
            GalleryPickerScreen(
                startVisible = true,
                onResult = { uri ->
                    showPicker = false
                    uri?.let { vm.onEvent(Event.OnAvatarPicked(it.toString())) }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateBack -> onBack()
                is Effect.ShowMessage -> {
                    effect.message
                }
            }
        }
    }

    BackHandler { vm.onEvent(Event.BackClicked) }

    AddContactScreenContent(
        state = state,
        onBack = { vm.onEvent(Event.BackClicked) },
        onAvatarClick = {
            pickerKey++
            showPicker = true
        },
        onSave = { vm.onEvent(Event.SaveClicked) },
        onUsernameChange = { vm.onEvent(Event.UsernameChanged(it)) },
        onCareerChange = { vm.onEvent(Event.CareerChanged(it)) },
        onEmailChange = { vm.onEvent(Event.EmailChanged(it)) },
        onPhoneChange = { vm.onEvent(Event.PhoneChanged(it)) },
        onAddressChange = { vm.onEvent(Event.AddressChanged(it)) },
        onDobChange = { vm.onEvent(Event.DateOfBirthChanged(it)) },
    )
}
