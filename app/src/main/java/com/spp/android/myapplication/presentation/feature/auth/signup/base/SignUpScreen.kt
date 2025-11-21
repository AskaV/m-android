package com.spp.android.myapplication.presentation.feature.auth.signup.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.NavigateToExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.NavigateToLogin
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.OpenGoogleSignIn
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.EmailChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PasswordChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.RegisterWithGoogle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.RememberChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.SubmitRegister
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    onOpenGoogle: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToExtended: (String) -> Unit,
    vm: SignUpViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackBar = SnackbarHostState()

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is ShowMessage -> snackBar.showSnackbar(effect.message)
                is OpenGoogleSignIn -> onOpenGoogle()
                is NavigateToLogin -> onNavigateToLogin()

                is NavigateToExtended -> {
                    val email = vm.state.value.fields.email
                    if (email.isNotBlank()) {
                        onNavigateToExtended(email)
                    }
                }

                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBar) }) { paddings ->
        SignUpScreenContent(
            state = state,
            onEmailChange = { vm.onEvent(EmailChanged(it)) },
            onPasswordChange = { vm.onEvent(PasswordChanged(it)) },
            onRememberMeChange = { vm.onEvent(RememberChanged(it)) },
            onRegisterClick = { vm.onEvent(SubmitRegister) },
            onRegisterWithGoogleClick = { vm.onEvent(RegisterWithGoogle) },
            onNavigateToLogin = { onNavigateToLogin() },
            modifier = Modifier.padding(paddings),
        )
    }
}
