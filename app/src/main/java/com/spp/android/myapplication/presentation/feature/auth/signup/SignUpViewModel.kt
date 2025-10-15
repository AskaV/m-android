package com.spp.android.myapplication.presentation.feature.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.utils.Validate
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.*
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.text
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

    private inline fun updateState(block: SignUpContract.State.() -> SignUpContract.State) {
        _state.update { it.block() }
    }

    private inline fun updateProfile(block: SignUpContract.ProfileState.() -> SignUpContract.ProfileState) {
        _profile.update { it.block() }
    }

    private fun sendEffect(e: SignUpContract.Effect) = viewModelScope.launch { _effect.send(e) }

    private enum class Field { EMAIL, PASSWORD, USERNAME, PHONE }

    private fun SignUpContract.State.clear(field: Field) = when (field) {
        Field.EMAIL -> copy(fields = fields.copy(emailError = null))
        Field.PASSWORD -> copy(fields = fields.copy(passwordError = null))
        else -> this
    }

    private fun SignUpContract.ProfileState.clear(field: Field) = when (field) {
        Field.USERNAME -> copy(usernameError = "")
        Field.PHONE -> copy(phoneError = "")
        else -> this
    }

    private fun SignUpContract.State.validate(field: Field) = when (field) {
        Field.EMAIL -> {
            val msg = Validate.email(fields.email)?.text(appContext)
            copy(fields = fields.copy(emailError = msg))
        }

        Field.PASSWORD -> {
            val msg = Validate.password(fields.password)?.text(appContext)
            copy(fields = fields.copy(passwordError = msg))
        }

        else -> this
    }

    private fun SignUpContract.ProfileState.validate(field: Field) = when (field) {
        Field.USERNAME -> copy(
            usernameError = Validate.username(username)?.text(appContext).orEmpty()
        )

        Field.PHONE -> copy(phoneError = Validate.phone(phone)?.text(appContext).orEmpty())
        else -> this
    }

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is EmailChanged -> updateState {
                copy(fields = fields.copy(email = event.value)).clear(
                    Field.EMAIL
                )
            }

            is PasswordChanged -> updateState {
                copy(fields = fields.copy(password = event.value)).clear(
                    Field.PASSWORD
                )
            }

            is RememberChanged -> updateState { copy(rememberMe = event.value) }

            is EmailBlur -> updateState { validate(Field.EMAIL) }
            is PasswordBlur -> updateState { validate(Field.PASSWORD) }

            is UsernameChanged -> updateProfile { copy(username = event.value).clear(Field.USERNAME) }
            is PhoneChanged -> updateProfile { copy(phone = event.value).clear(Field.PHONE) }
            is UsernameBlur -> updateProfile { validate(Field.USERNAME) }
            is PhoneBlur -> updateProfile { validate(Field.PHONE) }

            is AvatarPicked -> updateProfile { copy(avatar = event.uri) }
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

            is ErrorShown -> updateState { copy(error = "") }
        }
    }

    private fun submitRegister() = viewModelScope.launch {
        val s = _state.value
        val emailErr = Validate.email(s.fields.email.trim())?.text(appContext)
        val passErr = Validate.password(s.fields.password)?.text(appContext)

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
            sendEffect(SignUpContract.Effect.NavigateToExtended)
        }.onFailure { t ->
            val message = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
            updateState { copy(error = message) }
            sendEffect(
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
        val p = _profile.value
        val normalizedPhone = p.phone.replace(Regex("[^+\\d]"), "")

        val usernameErr = Validate.username(p.username)?.text(appContext)
        val phoneErr = Validate.phone(normalizedPhone)?.text(appContext)

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
            sendEffect(SignUpContract.Effect.NavigateToHome)
        }.onFailure { t ->
            val message = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
            updateState { copy(error = message) }
            sendEffect(
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