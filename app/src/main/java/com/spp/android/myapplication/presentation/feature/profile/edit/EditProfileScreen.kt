package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.*

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onDone: () -> Unit = {},
    vm: EditProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                NavigateBack -> onBack()
                Saved -> onDone()
                is ShowMessage -> {/* TODO snackbar */
                }

                OpenAvatarPicker -> {/* TODO open picker */
                }
            }
        }
    }

    EditProfileScreenContent(
        onBack = { vm.onEvent(EditProfileContract.Event.BackClicked) },
        onSave = { username, career, phone, address, birthdate ->
            vm.onEvent(EditProfileContract.Event.UsernameChanged(username))
            vm.onEvent(EditProfileContract.Event.CareerChanged(career))
            vm.onEvent(EditProfileContract.Event.PhoneChanged(phone))
            vm.onEvent(EditProfileContract.Event.AddressChanged(address))
            vm.onEvent(EditProfileContract.Event.BirthdateChanged(birthdate))
            vm.onEvent(EditProfileContract.Event.SaveClicked)
        })
}