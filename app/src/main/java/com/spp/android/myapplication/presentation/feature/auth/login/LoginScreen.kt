package com.spp.android.myapplication.presentation.feature.auth.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            when (eff) {
                is LoginContract.Effect.NavigateToHome -> onNavigateHome()
                is LoginContract.Effect.ShowMessage -> { /* TODO: show snackbar */
                }

                is LoginContract.Effect.ForgotPassword -> {
                    Toast.makeText(context, "Forgot password clicked", Toast.LENGTH_SHORT).show()
                }
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
        onForgotPasswordClick = { vm.onEvent(LoginContract.Event.ForgotPasswordClicked) },
        modifier = Modifier
    )
}