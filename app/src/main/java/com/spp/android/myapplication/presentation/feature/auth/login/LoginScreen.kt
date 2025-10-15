package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateHome: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    vm: LoginViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { effect ->
            when (effect) {
                is LoginContract.Effect.NavigateToHome -> {
                    val email = vm.state.value.email
                    if (!email.isNullOrBlank()) {
                        onNavigateHome(email)
                    }
                }

                is LoginContract.Effect.ForgotPassword -> onForgotPassword()
            }
        }
    }

    LoginScreenContent(
        state = state,
        onEmailChange = { vm.onEvent(LoginContract.Event.EmailChanged(it)) },
        onPasswordChange = { vm.onEvent(LoginContract.Event.PasswordChanged(it)) },
        onRememberMeChange = { vm.onEvent(LoginContract.Event.RememberChanged(it)) },
        onLoginClick = { vm.onEvent(LoginContract.Event.Submit) },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = { vm.onEvent(LoginContract.Event.ForgotPasswordClicked) })
}