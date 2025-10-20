package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Event

@Composable
fun AddContactScreen(
    onBack: () -> Unit = {},
    vm: AddContactViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                is Effect.NavigateBack -> onBack()
                is Effect.ShowMessage -> { /*TODO Show message to user, e.g.*/}
            }
        }
    }

    BackHandler { vm.onEvent(Event.BackClicked) }

    AddContactScreenContent(
        state = state,
        onBack = { vm.onEvent(Event.BackClicked) },
        onAvatarClick = { vm.onEvent(Event.AvatarClicked) },
        onSave = { vm.onEvent(Event.SaveClicked) },
        onUsernameChange = { vm.onEvent(Event.UsernameChanged(it)) },
        onCareerChange = { vm.onEvent(Event.CareerChanged(it)) },
        onEmailChange = { vm.onEvent(Event.EmailChanged(it)) },
        onPhoneChange = { vm.onEvent(Event.PhoneChanged(it)) },
        onAddressChange = { vm.onEvent(Event.AddressChanged(it)) },
        onDobChange = { vm.onEvent(Event.DateOfBirthChanged(it)) },
    )
}