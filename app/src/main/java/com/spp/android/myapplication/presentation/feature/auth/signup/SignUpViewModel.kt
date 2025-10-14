package com.spp.android.myapplication.presentation.feature.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.validation.validateEmail
import com.spp.android.myapplication.domain.validation.validatePassword
import com.spp.android.myapplication.domain.validation.validatePhone
import com.spp.android.myapplication.domain.validation.validateUsername
import com.spp.android.myapplication.presentation.texts.AppText
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
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpContract.State())
    val state: StateFlow<SignUpContract.State> = _state.asStateFlow()

    private val _profile = MutableStateFlow(SignUpContract.ProfileState())
    val profile: StateFlow<SignUpContract.ProfileState> = _profile.asStateFlow()

    private val _effect = Channel<SignUpContract.Effect>(Channel.BUFFERED)
    val effect: Flow<SignUpContract.Effect> = _effect.receiveAsFlow()

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.EmailChanged -> {
                _state.update {
                    it.copy(
                        fields = it.fields.copy(
                            email = event.value, emailError = null
                        )
                    )
                }
            }

            is SignUpContract.Event.PasswordChanged -> {
                _state.update {
                    it.copy(
                        fields = it.fields.copy(
                            password = event.value, passwordError = null
                        )
                    )
                }
            }

            is SignUpContract.Event.RememberChanged -> {
                _state.update { it.copy(rememberMe = event.value) }
            }

            is SignUpContract.Event.SubmitRegister -> submitRegister()
            is SignUpContract.Event.RegisterWithGoogle -> viewModelScope.launch {
                _effect.send(
                    SignUpContract.Effect.OpenGoogleSignIn
                )
            }

            is SignUpContract.Event.NavigateToExtendedRequested -> submitRegister()

            is SignUpContract.Event.ErrorShown -> _state.update { it.copy() }


            is SignUpContract.Event.UsernameChanged -> {
                _profile.update { it.copy(username = event.value) }
            }

            is SignUpContract.Event.PhoneChanged -> {
                _profile.update { it.copy(phone = event.value) }
            }

            is SignUpContract.Event.UsernameBlur -> {
                _profile.update { p ->
                    p.copy(
                        usernameError = validateUsername(
                            appContext, p.username
                        ).orEmpty()
                    )
                }
            }

            is SignUpContract.Event.PhoneBlur -> {
                _profile.update { p ->
                    p.copy(
                        phoneError = validatePhone(
                            appContext, p.phone
                        ).orEmpty()
                    )
                }
            }


            is SignUpContract.Event.PickAvatar -> viewModelScope.launch {
                _effect.send(
                    SignUpContract.Effect.OpenAvatarPicker
                )
            }

            is SignUpContract.Event.CancelExtended -> viewModelScope.launch {
                _profile.value = SignUpContract.ProfileState(); _state.value =
                SignUpContract.State();_effect.send(
                SignUpContract.Effect.BackFromExtended
            )
            }

            is SignUpContract.Event.ForwardExtended -> submitExtended()
            is SignUpContract.Event.EmailBlur -> {
                _state.update { s ->
                    s.copy(
                        fields = s.fields.copy(
                            emailError = validateEmail(
                                appContext, s.fields.email
                            )
                        )
                    )
                }
            }

            is SignUpContract.Event.PasswordBlur -> {
                _state.update { s ->
                    s.copy(
                        fields = s.fields.copy(
                            passwordError = validatePassword(
                                appContext, s.fields.password
                            )
                        )
                    )
                }
            }

            is SignUpContract.Event.AvatarPicked -> {
                _profile.update { it.copy(avatar = event.uri) }
            }
        }
    }

    private fun submitRegister() = viewModelScope.launch {
        val s = state.value

        val emailErr = validateEmail(appContext, s.fields.email.trim())
        val passErr = validatePassword(appContext, s.fields.password)

        if (emailErr != null || passErr != null) {
            _state.update {
                it.copy(
                    fields = it.fields.copy(
                        emailError = emailErr, passwordError = passErr
                    )
                )
            }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        runCatching {
            // TODO:
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToExtended)
        }.onFailure { t ->
            _state.update {
                it.copy(error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext))
            }
            _effect.send(
                SignUpContract.Effect.ShowMessage(
                    AppText.OtherInfo.LOGIN_FAILED.text(
                        appContext
                    )
                )
            )
        }

        _state.update { it.copy(isLoading = false) }
    }

    private fun submitExtended() = viewModelScope.launch {
        val p = profile.value

        val normalizedPhone = p.phone.replace(Regex("[^+\\d]"), "")

        val usernameErr = validateUsername(appContext, p.username)
        val phoneErr = validatePhone(appContext, normalizedPhone)
        if (usernameErr != null || phoneErr != null) {
            _profile.update {
                it.copy(
                )
            }
            return@launch
        }

        _profile.update {
            it.copy(
                isLoading = true
            )
        }

        runCatching {
            // TODO:
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToHome)
        }.onFailure { t ->
            _state.update {
                it.copy(
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(
                        appContext
                    )
                )
            }
            _effect.send(
                SignUpContract.Effect.ShowMessage(
                    AppText.OtherInfo.LOGIN_FAILED.text(
                        appContext
                    )
                )
            )
        }

        _profile.update { it.copy(isLoading = false) }
    }
}