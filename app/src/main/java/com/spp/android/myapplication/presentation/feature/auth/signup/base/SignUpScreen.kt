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
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    onOpenGoogle: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToExtended: (String) -> Unit,
    vm: SignUpViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = SnackbarHostState()

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                is SignUpContract.Effect.ShowMessage -> snackbar.showSnackbar(eff.message)
                SignUpContract.Effect.OpenGoogleSignIn -> onOpenGoogle()
                SignUpContract.Effect.NavigateToLogin -> onNavigateToLogin()

                SignUpContract.Effect.NavigateToExtended -> {
                    val email = vm.state.value.fields.email
                    if (!email.isNullOrBlank()) {
                        onNavigateToExtended(email)
                    }
                }

                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { paddings ->
        SignUpScreenContent(
            state = SignUpContract.State(fields = state.fields, rememberMe = state.rememberMe),
            onEmailChange = { vm.onEvent(SignUpContract.Event.EmailChanged(it)) },
            onPasswordChange = { vm.onEvent(SignUpContract.Event.PasswordChanged(it)) },
            onRememberMeChange = { vm.onEvent(SignUpContract.Event.RememberChanged(it)) },
            onRegisterClick = { vm.onEvent(SignUpContract.Event.SubmitRegister) },
            onRegisterWithGoogleClick = { vm.onEvent(SignUpContract.Event.RegisterWithGoogle) },
            onNavigateToLogin = { onNavigateToLogin() },
            modifier = Modifier.padding(paddings)
        )
    }
}