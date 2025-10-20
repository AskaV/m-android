package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.Clear
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.EmailBlur
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.EmailChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.ForgotPasswordClicked
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.PasswordBlur
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.PasswordChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.RememberChanged
import com.spp.android.myapplication.presentation.feature.auth.login.LoginContract.Event.Submit
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.utils.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
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
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect: Flow<LoginContract.Effect> = _effect.receiveAsFlow()

    private inline fun updateState(block: LoginContract.State.() -> LoginContract.State) {
        _state.update { it.block() }
    }

    private enum class Field { EMAIL, PASSWORD }

    private fun LoginContract.State.clear(field: Field): LoginContract.State = when (field) {
        Field.EMAIL -> copy(email = email, emailErrorKey = null, error = null)
        Field.PASSWORD -> copy(password = password, passwordErrorKey = null, error = null)
    }

    private fun LoginContract.State.validate(field: Field): LoginContract.State = when (field) {
        Field.EMAIL -> copy(emailErrorKey = Validate.email(email))
        Field.PASSWORD -> copy(passwordErrorKey = Validate.password(password))
    }

    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is EmailChanged -> updateState { copy(email = event.email).clear(Field.EMAIL) }
            is PasswordChanged -> updateState { copy(password = event.password).clear(Field.PASSWORD) }
            is RememberChanged -> updateState { copy(rememberMe = event.isChecked) }

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
        val emailErrKey = Validate.email(_state.value.email.trim())
        val passErrKey = Validate.password(_state.value.password)

        if (emailErrKey != null || passErrKey != null) {
            updateState { copy(emailErrorKey = emailErrKey, passwordErrorKey = passErrKey) }
            return@launch
        }

        updateState { copy(isLoading = true, errorKey = null) }

        runCatching {
            Unit
        }.onSuccess {
            _effect.send(LoginContract.Effect.NavigateToHome)
        }.onFailure { _ ->
            updateState { copy(errorKey = AppText.OtherInfo.UNKNOWN_ERROR) }
        }

        updateState { copy(isLoading = false) }
    }
}