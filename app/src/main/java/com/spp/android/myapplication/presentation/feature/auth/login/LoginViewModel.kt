package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect: Flow<LoginContract.Effect> = _effect.receiveAsFlow()

    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.EmailChanged -> {
                _state.update { it.copy(email = event.value, error = null) }
            }
            is LoginContract.Event.PasswordChanged -> {
                _state.update { it.copy(password = event.value, error = null) }
            }
            is LoginContract.Event.RememberChanged -> {
                _state.update { it.copy(rememberMe = event.value) }
            }

            LoginContract.Event.EmailBlur -> Unit
            LoginContract.Event.PasswordBlur -> Unit

            LoginContract.Event.Submit -> submit()
            LoginContract.Event.ErrorShown -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null, emailError = null, passwordError = null) }

        runCatching {
            // TODO: Replace with real login call
        }.onSuccess {
            _effect.send(LoginContract.Effect.NavigateToHome)
        }.onFailure { t ->
            _state.update { it.copy(error = t.message ?: "Unknown error") }
            _effect.send(LoginContract.Effect.ShowMessage("Login failed"))
        }

        _state.update { it.copy(isLoading = false) }
    }
}