package com.spp.android.myapplication.presentation.feature.auth.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Effect.ForgotPassword
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Effect.NavigateToHome
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Effect.ShowToast
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
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToHome -> {
                    val email = viewModel.state.value.email
                    if (email.isNotBlank()) {
                        onNavigateHome(email)
                    }
                }

                is ForgotPassword -> onForgotPassword()
                is ShowToast -> Toast.makeText(context, effect.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginScreenContent(
        state = state,
        onEmailChange = { viewModel.onEvent(EmailChanged(it)) },
        onPasswordChange = { viewModel.onEvent(PasswordChanged(it)) },
        onRememberMeChange = { viewModel.onEvent(RememberChanged(it)) },
        onLoginClick = { viewModel.onEvent(Submit) },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = { viewModel.onEvent(ForgotPasswordClicked) },
    )
}
