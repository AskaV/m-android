package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.OpenAvatarPicker
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.Saved
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.AddressChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.BirthdateChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.CareerChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.PhoneChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.SaveClicked
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.UsernameChanged

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onDone: () -> Unit = {},
    onOpenAvatarPicker: () -> Unit = {},
    vm: EditProfileViewModel = hiltViewModel()
) {
    val snackBar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                NavigateBack -> onBack()
                Saved -> onDone()
                is ShowMessage -> {
                    snackBar.showSnackbar(effect.messageKey.text(context))
                }

                OpenAvatarPicker -> onOpenAvatarPicker()

            }
        }
    }

    EditProfileScreenContent(
        onBack = { vm.onEvent(BackClicked) },
        onSave = { username, career, phone, address, birthdate ->
            vm.onEvent(UsernameChanged(username))
            vm.onEvent(CareerChanged(career))
            vm.onEvent(PhoneChanged(phone))
            vm.onEvent(AddressChanged(address))
            vm.onEvent(BirthdateChanged(birthdate))
            vm.onEvent(SaveClicked)
        })
}