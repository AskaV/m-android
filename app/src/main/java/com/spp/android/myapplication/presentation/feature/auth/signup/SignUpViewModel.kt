package com.spp.android.myapplication.presentation.feature.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.validation.validateEmail
import com.spp.android.myapplication.domain.validation.validatePassword
import com.spp.android.myapplication.domain.validation.validatePhone
import com.spp.android.myapplication.domain.validation.validateUsername
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.*
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

    // --- единые мутаторы состояния ---
    private inline fun updateState(block: SignUpContract.State.() -> SignUpContract.State) {
        _state.update { it.block() }
    }

    private inline fun updateProfile(block: SignUpContract.ProfileState.() -> SignUpContract.ProfileState) {
        _profile.update { it.block() }
    }

    private enum class Field { EMAIL, PASSWORD, USERNAME, PHONE }

    private fun SignUpContract.State.clear(field: Field): SignUpContract.State = when (field) {
        Field.EMAIL -> copy(fields = fields.copy(emailError = null))
        Field.PASSWORD -> copy(fields = fields.copy(passwordError = null))
        else -> this
    }

    private fun SignUpContract.ProfileState.clear(field: Field): SignUpContract.ProfileState =
        when (field) {
            Field.USERNAME -> copy(usernameError = "")
            Field.PHONE -> copy(phoneError = "")
            else -> this

        }

    private fun SignUpContract.State.validate(field: Field): SignUpContract.State = when (field) {
        Field.EMAIL -> copy(
            fields = fields.copy(
                emailError = validateEmail(
                    appContext, fields.email
                )
            )
        )

        Field.PASSWORD -> copy(
            fields = fields.copy(
                passwordError = validatePassword(
                    appContext, fields.password
                )
            )
        )

        else -> this
    }

    private fun SignUpContract.ProfileState.validate(field: Field): SignUpContract.ProfileState =
        when (field) {
            Field.USERNAME -> copy(usernameError = validateUsername(appContext, username).orEmpty())
            Field.PHONE -> copy(phoneError = validatePhone(appContext, phone).orEmpty())
            else -> this
        }

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is EmailChanged -> updateState {
                copy(fields = fields.copy(email = event.value)).clear(Field.EMAIL)
            }

            is PasswordChanged -> updateState {
                copy(fields = fields.copy(password = event.value)).clear(Field.PASSWORD)
            }

            is RememberChanged -> updateState { copy(rememberMe = event.value) }
            is EmailBlur -> updateState { validate(Field.EMAIL) }
            is PasswordBlur -> updateState { validate(Field.PASSWORD) }

            is UsernameChanged -> updateProfile { copy(username = event.value).clear(Field.USERNAME) }

            is PhoneChanged -> updateProfile { copy(phone = event.value).clear(Field.PHONE) }

            is UsernameBlur -> updateProfile { validate(Field.USERNAME) }
            is PhoneBlur -> updateProfile { validate(Field.PHONE) }
            is AvatarPicked -> updateProfile { copy(avatar = event.uri) }

            is PickAvatar -> viewModelScope.launch {
                _effect.send(SignUpContract.Effect.OpenAvatarPicker)
            }

            is RegisterWithGoogle -> viewModelScope.launch {
                _effect.send(SignUpContract.Effect.OpenGoogleSignIn)
            }

            is SubmitRegister -> submitRegister()
            is ForwardExtended -> submitExtended()
            is NavigateToExtendedRequested -> submitRegister()
            is CancelExtended -> viewModelScope.launch {
                updateProfile { SignUpContract.ProfileState() }
                updateState { SignUpContract.State() }
                _effect.send(SignUpContract.Effect.BackFromExtended)
            }

            is ErrorShown -> updateState { copy(error = "") }
        }
    }

    private fun submitRegister() = viewModelScope.launch {
        val s = _state.value
        val emailErr = validateEmail(appContext, s.fields.email.trim())
        val passErr = validatePassword(appContext, s.fields.password)

        if (emailErr != null || passErr != null) {
            updateState {
                copy(
                    fields = fields.copy(
                        emailError = emailErr, passwordError = passErr
                    )
                )
            }
            return@launch
        }

        updateState { copy(isLoading = true, error = "") }

        runCatching {
            Unit
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToExtended)
        }.onFailure { t ->
            val message = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
            updateState { copy(error = message) }
            _effect.send(
                SignUpContract.Effect.ShowMessage(
                    AppText.OtherInfo.LOGIN_FAILED.text(
                        appContext
                    )
                )
            )
        }

        updateState { copy(isLoading = false) }
    }

    private fun submitExtended() = viewModelScope.launch {
        val normalizedPhone = _profile.value.phone.replace(Regex("[^+\\d]"), "")

        val usernameErr = validateUsername(appContext, _profile.value.username)
        val phoneErr = validatePhone(appContext, normalizedPhone)

        if (usernameErr != null || phoneErr != null) {
            updateProfile {
                copy(
                    usernameError = usernameErr.orEmpty(), phoneError = phoneErr.orEmpty()
                )
            }
            return@launch
        }

        updateProfile { copy(isLoading = true) }

        runCatching {
            Unit
        }.onSuccess {
            _effect.send(SignUpContract.Effect.NavigateToHome)
        }.onFailure { t ->
            val message = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
            updateState { copy(error = message) }
            _effect.send(
                SignUpContract.Effect.ShowMessage(
                    AppText.OtherInfo.LOGIN_FAILED.text(
                        appContext
                    )
                )
            )
        }

        updateProfile { copy(isLoading = false) }
    }
}