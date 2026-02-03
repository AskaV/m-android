package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerScreen
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.NavigateBack
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.OpenAvatarPicker
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.Saved
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.AddressChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.AvatarSelected
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.BirthdateChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.CareerChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.PhoneChanged
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.SaveClicked
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event.UsernameChanged
import java.io.Serializable

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onDone: (ProfileResult) -> Unit = {},
    vm: EditProfileViewModel = hiltViewModel(),
) {
    val snackBar = remember { SnackbarHostState() }
    val context = LocalContext.current
    val state by vm.state.collectAsState()

    var showPicker by remember { mutableStateOf(false) }
    var pickerKey by remember { mutableIntStateOf(0) }

    if (showPicker) {
        key(pickerKey) {
            GalleryPickerScreen(
                startVisible = true,
                onResult = { uri ->
                    showPicker = false
                    uri?.let { vm.onEvent(AvatarSelected(it.toString())) }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is NavigateBack -> onBack()

                is Saved ->
                    onDone(
                        ProfileResult(
                            username = state.username,
                            career = state.career,
                            phone = state.phone,
                            address = state.address,
                            birthdate = state.birthdate,
                        ),
                    )

                is ShowMessage -> {
                    snackBar.showSnackbar(effect.messageKey.text(context))
                }

                is OpenAvatarPicker -> {
                    pickerKey++
                    showPicker = true
                }
            }
        }
    }

    EditProfileScreenContent(
        state = state,
        onBack = { vm.onEvent(BackClicked) },
        onValueChange = vm::onFieldChange,
        onSave = { username, career, phone, address, birthdate ->
            vm.onEvent(UsernameChanged(username))
            vm.onEvent(CareerChanged(career))
            vm.onEvent(PhoneChanged(phone))
            vm.onEvent(AddressChanged(address))
            vm.onEvent(BirthdateChanged(birthdate))
            vm.onEvent(SaveClicked)
        },
        onAvatarClick = { vm.onEvent(EditProfileContract.Event.AvatarClicked) },
    )
}

data class ProfileResult(
    val username: String,
    val career: String,
    val phone: String,
    val address: String,
    val birthdate: String,
) : Serializable
