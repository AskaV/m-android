package com.spp.android.myapplication.presentation.feature.auth.signup.extended

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpExtendedScreen(
    onBack: () -> Unit,
    onOpenAvatarPicker: () -> Unit,
    onNavigateHome: () -> Unit,
    vm: SignUpViewModel = hiltViewModel()
) {
    val profile by vm.profile.collectAsStateWithLifecycle()
    val snackbar = SnackbarHostState()

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                is SignUpContract.Effect.ShowMessage -> snackbar.showSnackbar(eff.message)
                SignUpContract.Effect.OpenAvatarPicker -> onOpenAvatarPicker()
                SignUpContract.Effect.BackFromExtended -> onBack()
                SignUpContract.Effect.NavigateToHome -> onNavigateHome()
                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { paddings ->
        SignUpProfileScreenContent(
            state = SignUpProfileUiState(
                username = profile.username,
                phone = profile.phone,
                usernameError = profile.usernameError,
                phoneError = profile.phoneError
            ),
            onUserNameChange = { vm.onEvent(SignUpContract.Event.UsernameChanged(it)) },
            onPhoneChange = { vm.onEvent(SignUpContract.Event.PhoneChanged(it)) },
            onPickAvatar = { vm.onEvent(SignUpContract.Event.PickAvatar) },
            onCancel = { vm.onEvent(SignUpContract.Event.CancelExtended) },
            onForward = { vm.onEvent(SignUpContract.Event.ForwardExtended) },
            modifier = Modifier.padding(paddings)
        )
    }
}