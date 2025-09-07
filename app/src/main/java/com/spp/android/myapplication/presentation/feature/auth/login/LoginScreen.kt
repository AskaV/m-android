package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                LoginContract.Effect.NavigateToHome -> onNavigateHome()
                is LoginContract.Effect.ShowMessage -> { /* TODO: show snackbar */ }
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
        modifier = Modifier
    )
}