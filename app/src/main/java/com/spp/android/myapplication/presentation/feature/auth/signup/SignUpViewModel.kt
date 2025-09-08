package com.spp.android.myapplication.presentation.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignUpContract.State())
    val state: StateFlow<SignUpContract.State> = _state.asStateFlow()

    private val _profile = MutableStateFlow(SignUpContract.ProfileState())
    val profile: StateFlow<SignUpContract.ProfileState> = _profile.asStateFlow()

    private val _effect = Channel<SignUpContract.Effect>(Channel.BUFFERED)
    val effect: Flow<SignUpContract.Effect> = _effect.receiveAsFlow()

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.EmailChanged -> {
                _state.update { it.copy(fields = it.fields.copy(email = event.value)) }
            }

            is SignUpContract.Event.PasswordChanged -> {
                _state.update { it.copy(fields = it.fields.copy(password = event.value)) }
            }

            is SignUpContract.Event.RememberChanged -> {
                _state.update { it.copy(rememberMe = event.value) }
            }


            SignUpContract.Event.SubmitRegister -> submitRegister()
            SignUpContract.Event.RegisterWithGoogle ->
                viewModelScope.launch { _effect.send(SignUpContract.Effect.OpenGoogleSignIn) }

            SignUpContract.Event.NavigateToExtendedRequested ->
                viewModelScope.launch { _effect.send(SignUpContract.Effect.NavigateToExtended) }

            SignUpContract.Event.ErrorShown ->
                _state.update { it.copy(error = null) }

            is SignUpContract.Event.UsernameChanged -> {
                _profile.update { it.copy(username = event.value) }
            }

            is SignUpContract.Event.PhoneChanged -> {
                _profile.update { it.copy(phone = event.value) }
            }

            SignUpContract.Event.UsernameBlur -> Unit
            SignUpContract.Event.PhoneBlur -> Unit


            SignUpContract.Event.PickAvatar ->
                viewModelScope.launch { _effect.send(SignUpContract.Effect.OpenAvatarPicker) }

            SignUpContract.Event.CancelExtended ->
                viewModelScope.launch { _effect.send(SignUpContract.Effect.BackFromExtended) }

            SignUpContract.Event.ForwardExtended -> submitExtended()
            SignUpContract.Event.EmailBlur -> TODO()
            SignUpContract.Event.PasswordBlur -> TODO()
        }
    }

    private fun submitRegister() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                fields = it.fields.copy(emailError = null, passwordError = null)
            )
        }

        runCatching {
            // TODO:
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToExtended)
        }.onFailure { t ->
            _state.update { it.copy(error = t.message ?: "Unknown error") }
            _effect.send(SignUpContract.Effect.ShowMessage("Registration failed"))
        }

        _state.update { it.copy(isLoading = false) }
    }

    private fun submitExtended() = viewModelScope.launch {
        _profile.update {
            it.copy(
                isLoading = true,
                error = null,
                usernameError = null,
                phoneError = null
            )
        }

        runCatching {
            // TODO: completeProfileUseCase(...)
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToHome)
        }.onFailure { t ->
            _state.update { it.copy(error = t.message ?: "Unknown error") }
            _effect.send(SignUpContract.Effect.ShowMessage("Saving profile failed"))
        }

        _profile.update { it.copy(isLoading = false) }
    }
}