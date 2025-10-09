package com.spp.android.myapplication.presentation.feature.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.validation.validateEmail
import com.spp.android.myapplication.domain.validation.validatePassword
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect: Flow<LoginContract.Effect> = _effect.receiveAsFlow()


    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.EmailChanged -> { _state.update { it.copy(email = event.value, emailError = null, error = null) } }
            is LoginContract.Event.PasswordChanged -> { _state.update { it.copy(password = event.value, passwordError = null, error = null) } }
            is LoginContract.Event.RememberChanged -> { _state.update { it.copy(rememberMe = event.value) } }

            LoginContract.Event.EmailBlur -> { _state.update { s ->  s.copy(emailError = validateEmail(appContext, s.email)) } }
            LoginContract.Event.PasswordBlur -> { _state.update { s ->  s.copy(passwordError  = validateEmail(appContext, s.password)) } }

            LoginContract.Event.ForgotPasswordClicked -> { viewModelScope.launch { _effect.send(LoginContract.Effect.ForgotPassword) } }

            LoginContract.Event.Submit -> submit()
            LoginContract.Event.ErrorShown -> _state.update { it.copy(error = null) }
            LoginContract.Event.Clear -> _state.value = LoginContract.State()
        }
    }

    private fun submit() = viewModelScope.launch {
        val s = state.value
        val emailErr = validateEmail(appContext, s.email)
        val passErr = validatePassword(appContext,s.password)

        if (emailErr != null || passErr != null) {
            _state.update { it.copy(emailError = emailErr, passwordError = passErr) }
            return@launch
        }

        _state.update { it.copy(isLoading = true, error = null) }

        runCatching {
            // TODO:
        }.onSuccess {
            _effect.send(LoginContract.Effect.NavigateToHome)
        }.onFailure { t ->
            _state.update { it.copy(error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)) }
        }

        _state.update { it.copy(isLoading = false) }
    }
}