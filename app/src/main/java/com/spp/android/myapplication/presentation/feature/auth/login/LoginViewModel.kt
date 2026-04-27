package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.domain.repository.AuthRepository
import com.spp.android.myapplication.domain.storage.LocalStorage
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val localStorage: LocalStorage,
        private val authRepository: AuthRepository,
        private val authPreferences: AuthPreferences,
    ) : ViewModel() {
        private val useRemote = true

        private val _state = MutableStateFlow(LoginContract.State())
        val state: StateFlow<LoginContract.State> = _state.asStateFlow()

        private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
        val effect: Flow<LoginContract.Effect> = _effect.receiveAsFlow()

        private inline fun updateState(block: LoginContract.State.() -> LoginContract.State) {
            _state.update { it.block() }
        }

        init {
            viewModelScope.launch {
                localStorage.rememberMe.collect { remember ->
                    if (remember) {
                        val savedEmail = localStorage.savedEmail.first()
                        updateState { copy(email = savedEmail, rememberMe = true) }
                    }
                }
            }
        }

        private enum class Field { EMAIL, PASSWORD }

        private fun LoginContract.State.clear(field: Field): LoginContract.State =
            when (field) {
                Field.EMAIL -> copy(email = email, emailErrorKey = null, error = null)
                Field.PASSWORD -> copy(password = password, passwordErrorKey = null, error = null)
            }

        private fun LoginContract.State.validate(field: Field): LoginContract.State =
            when (field) {
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

        private fun submit() =
            viewModelScope.launch {
                val email = _state.value.email.trim()
                val password = _state.value.password

                val emailErrKey = Validate.email(email)
                val passErrKey = Validate.password(password)

                if (emailErrKey != null || passErrKey != null) {
                    updateState { copy(emailErrorKey = emailErrKey, passwordErrorKey = passErrKey) }
                    return@launch
                }

                updateState { copy(isLoading = true, errorKey = null, error = null) }

                runCatching {
                    if (useRemote) {
                        val auth = authRepository.login(email, password).getOrElse { throw it }

                        authPreferences.saveTokens(auth.accessToken, auth.refreshToken)
                        authPreferences.saveUserId(auth.user.id)
                    }

                    if (_state.value.rememberMe) {
                        localStorage.saveUser(email, true)
                    } else {
                        localStorage.saveUser("", false)
                    }
                }.onSuccess {
                    _effect.send(LoginContract.Effect.NavigateToHome)
                    println("LOGIN: userId=${authPreferences.userId.first()} accessLen=${authPreferences.accessToken.first().length}")
                }.onFailure { e ->
                    val msg = e.message?.takeIf { it.isNotBlank() } ?: "Unauthorized"
                    updateState { copy(errorKey = AppText.OtherInfo.UNKNOWN_ERROR, error = msg) }
                    _effect.send(LoginContract.Effect.ShowToast(msg))
                }

                updateState { copy(isLoading = false) }
            }
    }
