package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Effect.ForgotPassword
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Effect.NavigateToHome
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.EmailChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.ForgotPasswordClicked
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.PasswordChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.RememberChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.Submit
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
                is NavigateToHome -> {
                    val email = vm.state.value.email
                    if (email.isNotBlank()) {
                        onNavigateHome(email)
                    }
                }

                is ForgotPassword -> onForgotPassword()
            }
        }
    }

    LoginScreenContent(
        state = state,
        onEmailChange = { vm.onEvent(EmailChanged(it)) },
        onPasswordChange = { vm.onEvent(PasswordChanged(it)) },
        onRememberMeChange = { vm.onEvent(RememberChanged(it)) },
        onLoginClick = { vm.onEvent(Submit) },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = { vm.onEvent(ForgotPasswordClicked) })
}