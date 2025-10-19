package com.spp.android.myapplication.presentation.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.AvatarPicked
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.CancelExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.EmailBlur
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.EmailChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.ForwardExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.NavigateToExtendedRequested
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PasswordBlur
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PasswordChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PhoneBlur
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PhoneChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PickAvatar
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.RegisterWithGoogle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.RememberChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.SubmitRegister
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.UsernameBlur
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.UsernameChanged
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
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignUpContract.State())
    val state: StateFlow<SignUpContract.State> = _state.asStateFlow()

    private val _profile = MutableStateFlow(SignUpContract.ProfileState())
    val profile: StateFlow<SignUpContract.ProfileState> = _profile.asStateFlow()

    private val _effect = Channel<SignUpContract.Effect>(Channel.BUFFERED)
    val effect: Flow<SignUpContract.Effect> = _effect.receiveAsFlow()

    private inline fun updateState(block: SignUpContract.State.() -> SignUpContract.State) {
        _state.update { it.block() }
    }

    private inline fun updateProfile(block: SignUpContract.ProfileState.() -> SignUpContract.ProfileState) {
        _profile.update { it.block() }
    }

    private fun sendEffect(e: SignUpContract.Effect) = viewModelScope.launch { _effect.send(e) }

    private enum class Field { EMAIL, PASSWORD, USERNAME, PHONE }

    private fun SignUpContract.State.clear(field: Field) = when (field) {
        Field.EMAIL -> copy(fields = fields.copy(emailErrorKey = null))
        Field.PASSWORD -> copy(fields = fields.copy(passwordErrorKey = null))
        else -> this
    }

    private fun SignUpContract.ProfileState.clear(field: Field) = when (field) {
        Field.USERNAME -> copy(usernameErrorKey = null)
        Field.PHONE -> copy(phoneErrorKey = null)
        else -> this
    }

    private fun SignUpContract.State.validate(field: Field) = when (field) {
        Field.EMAIL -> copy(fields = fields.copy(emailErrorKey = Validate.email(fields.email)))
        Field.PASSWORD -> copy(fields = fields.copy(passwordErrorKey = Validate.password(fields.password)))
        else -> this
    }

    private fun SignUpContract.ProfileState.validate(field: Field) = when (field) {
        Field.USERNAME -> copy(usernameErrorKey = Validate.username(username))
        Field.PHONE -> copy(phoneErrorKey = Validate.phone(phone))
        else -> this
    }

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is EmailChanged -> updateState {
                copy(fields = fields.copy(email = event.email)).clear(
                    Field.EMAIL
                )
            }

            is PasswordChanged -> updateState {
                copy(fields = fields.copy(password = event.password)).clear(
                    Field.PASSWORD
                )
            }

            is RememberChanged -> updateState { copy(rememberMe = event.isChecked) }

            is EmailBlur -> updateState { validate(Field.EMAIL) }
            is PasswordBlur -> updateState { validate(Field.PASSWORD) }

            is UsernameChanged -> updateProfile { copy(username = event.username).clear(Field.USERNAME) }
            is PhoneChanged -> updateProfile { copy(phone = event.phone).clear(Field.PHONE) }
            is UsernameBlur -> updateProfile { validate(Field.USERNAME) }
            is PhoneBlur -> updateProfile { validate(Field.PHONE) }

            is AvatarPicked -> updateProfile { copy(avatar = event.avatarUri) }
            is PickAvatar -> sendEffect(SignUpContract.Effect.OpenAvatarPicker)
            is RegisterWithGoogle -> sendEffect(SignUpContract.Effect.OpenGoogleSignIn)

            is SubmitRegister -> submitRegister()
            is ForwardExtended -> submitExtended()
            is NavigateToExtendedRequested -> submitRegister()

            is CancelExtended -> viewModelScope.launch {
                updateProfile { SignUpContract.ProfileState() }
                updateState { SignUpContract.State() }
                sendEffect(SignUpContract.Effect.BackFromExtended)
            }

            is ErrorShown -> updateState { copy(errorKey = null) }
        }
    }

    private fun submitRegister() = viewModelScope.launch {
        val emailErrKey = Validate.email(_state.value.fields.email.trim())
        val passErrKey = Validate.password(_state.value.fields.password)

        if (emailErrKey != null || passErrKey != null) {
            updateState {
                copy(
                    fields = fields.copy(
                        emailErrorKey = emailErrKey, passwordErrorKey = passErrKey
                    )
                )
            }
            return@launch
        }

        updateState { copy(isLoading = true, errorKey = null) }

        runCatching { Unit }.onSuccess { sendEffect(SignUpContract.Effect.NavigateToExtended) }
            .onFailure { updateState { copy(errorKey = AppText.OtherInfo.UNKNOWN_ERROR) } }

        updateState { copy(isLoading = false) }

    }

    private fun submitExtended() = viewModelScope.launch {
        val normalizedPhone = _profile.value.phone.replace(Regex("[^+\\d]"), "")

        val usernameErrKey = Validate.username(_profile.value.username)
        val phoneErrKey = Validate.phone(normalizedPhone)

        if (usernameErrKey != null || phoneErrKey != null) {
            updateProfile { copy(usernameErrorKey = usernameErrKey, phoneErrorKey = phoneErrKey) }
            return@launch
        }

        updateProfile { copy(isLoading = true) }

        runCatching { Unit }.onSuccess { sendEffect(SignUpContract.Effect.NavigateToHome) }
            .onFailure { updateState { copy(errorKey = AppText.OtherInfo.UNKNOWN_ERROR) } }

        updateProfile { copy(isLoading = false) }
    }
}