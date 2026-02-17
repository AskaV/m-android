package com.spp.android.myapplication.presentation.feature.auth.signup.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract
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
import com.spp.android.myapplication.presentation.navigation.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    onOpenGoogle: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToExtended: (String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBar = SnackbarHostState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ShowMessage -> snackBar.showSnackbar(effect.message.text(context))
                is OpenGoogleSignIn -> onOpenGoogle()
                is NavigateToLogin -> onNavigateToLogin()

                is NavigateToExtended -> {
                    val email = viewModel.state.value.fields.email
                    if (email.isNotBlank()) {
                        onNavigateToExtended(email)
                    }
                }

                is SignUpContract.Effect.ShowToast -> {
                    showToast(context, effect.text)
                }

                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBar) }) { paddings ->
        SignUpScreenContent(
            state = state,
            onEmailChange = { viewModel.onEvent(EmailChanged(it)) },
            onPasswordChange = { viewModel.onEvent(PasswordChanged(it)) },
            onRememberMeChange = { viewModel.onEvent(RememberChanged(it)) },
            onRegisterClick = { viewModel.onEvent(SubmitRegister) },
            onRegisterWithGoogleClick = { viewModel.onEvent(RegisterWithGoogle) },
            onNavigateToLogin = { onNavigateToLogin() },
            modifier = Modifier.padding(paddings),
        )
    }
}
