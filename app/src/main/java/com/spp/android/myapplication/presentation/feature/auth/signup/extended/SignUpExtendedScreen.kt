package com.spp.android.myapplication.presentation.feature.auth.signup.extended

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpExtendedScreen(
    onBack: () -> Unit,
    onNavigateHome: () -> Unit = {},
    vm: SignUpViewModel = hiltViewModel(),
) {
    val profile by vm.profile.collectAsStateWithLifecycle()
    val snackbar = SnackbarHostState()
    var showPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                is SignUpContract.Effect.ShowMessage -> snackbar.showSnackbar(eff.message)
                SignUpContract.Effect.OpenAvatarPicker -> showPicker = true
                SignUpContract.Effect.BackFromExtended -> onBack()
                SignUpContract.Effect.NavigateToHome -> onNavigateHome()
                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { paddings ->
        SignUpProfileScreenContent(
            state = SignUpContract.ProfileState(
                username = profile.username,
                phone = profile.phone,
                usernameError = profile.usernameError,
                phoneError = profile.phoneError,
                avatar = profile.avatar
            ),
            onPickAvatar = { vm.onEvent(SignUpContract.Event.PickAvatar) },
            onUserNameChange = { vm.onEvent(SignUpContract.Event.UsernameChanged(it)) },
            onPhoneChange = { vm.onEvent(SignUpContract.Event.PhoneChanged(it)) },
            onCancel = { vm.onEvent(SignUpContract.Event.CancelExtended) },
            onForward = { vm.onEvent(SignUpContract.Event.ForwardExtended) },
            modifier = Modifier.padding(paddings)
        )
    }

    if (showPicker) {
        GalleryPickerScreen(
            startVisible = true, onResult = { uri ->
                showPicker = false
                uri?.let { vm.onEvent(SignUpContract.Event.AvatarPicked(it)) }
            })
    }
}