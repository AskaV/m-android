package com.spp.android.myapplication.presentation.feature.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.*
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.text
import com.spp.android.myapplication.presentation.utils.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private inline fun updateState(block: LoginContract.State.() -> LoginContract.State) {
        _state.update { it.block() }
    }

    private enum class Field { EMAIL, PASSWORD }

    private fun LoginContract.State.clear(field: Field): LoginContract.State = when (field) {
        Field.EMAIL -> copy(email = email, emailError = null, error = null)
        Field.PASSWORD -> copy(password = password, passwordError = null, error = null)
    }

    private fun LoginContract.State.validate(field: Field): LoginContract.State = when (field) {
        Field.EMAIL -> {
            val msg = Validate.email(email)?.text(appContext)
            copy(emailError = msg)
        }

        Field.PASSWORD -> {
            val msg = Validate.password(password)?.text(appContext)
            copy(passwordError = msg)
        }
    }

    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is EmailChanged -> updateState { copy(email = event.value).clear(Field.EMAIL) }
            is PasswordChanged -> updateState { copy(password = event.value).clear(Field.PASSWORD) }
            is RememberChanged -> updateState { copy(rememberMe = event.value) }

            is EmailBlur -> updateState { validate(Field.EMAIL) }
            is PasswordBlur -> updateState { validate(Field.PASSWORD) }

            is ErrorShown -> updateState { copy(error = null) }
            is Clear -> updateState { LoginContract.State() }

            is ForgotPasswordClicked -> {
                viewModelScope.launch { _effect.send(LoginContract.Effect.ForgotPassword) }
            }

            is Submit -> submit()
        }
    }

    private fun submit() = viewModelScope.launch {
        val s = _state.value
        val emailErr = Validate.email(s.email.trim())?.text(appContext)
        val passErr = Validate.password(s.password)?.text(appContext)

        if (emailErr != null || passErr != null) {
            updateState { copy(emailError = emailErr, passwordError = passErr) }
            return@launch
        }

        updateState { copy(isLoading = true, error = null) }

        runCatching {
            Unit
        }.onSuccess {
            _effect.send(LoginContract.Effect.NavigateToHome)
        }.onFailure { t ->
            val message = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
            updateState { copy(error = message) }
        }

        updateState { copy(isLoading = false) }
    }
}