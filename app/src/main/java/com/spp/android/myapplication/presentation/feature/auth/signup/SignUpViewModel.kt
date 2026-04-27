package com.spp.android.myapplication.presentation.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.domain.model.UserProfile
import com.spp.android.myapplication.domain.repository.AuthRepository
import com.spp.android.myapplication.domain.storage.AvatarStorage
import com.spp.android.myapplication.domain.storage.LocalStorage
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.BackFromExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.NavigateToExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.NavigateToHome
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.OpenAvatarPicker
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.OpenGoogleSignIn
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val localStorage: LocalStorage,
        private val avatarStorage: AvatarStorage,
        private val authRepository: AuthRepository,
        private val authPreferences: AuthPreferences,
    ) : ViewModel() {
        private val useRemote = true

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

        init {
            viewModelScope.launch {
                localStorage.rememberMe.collect { remember ->
                    if (remember) {
                        val savedEmail = localStorage.savedEmail.first()
                        updateState {
                            copy(
                                rememberMe = true,
                                fields = fields.copy(email = savedEmail),
                            )
                        }
                    }
                }
            }

            viewModelScope.launch {
                val saved = localStorage.userProfile.first()
                if (saved != null) {
                    updateProfile {
                        copy(
                            username = username.ifBlank { saved.username },
                            phone = phone.ifBlank { saved.phone },
                            avatarPath = if (avatarPath.isNullOrBlank()) saved.avatarPath else avatarPath,
                        )
                    }
                }
            }
        }

        private enum class Field { EMAIL, PASSWORD, USERNAME, PHONE }

        private fun SignUpContract.State.clear(field: Field) =
            when (field) {
                Field.EMAIL -> copy(fields = fields.copy(emailErrorKey = null))
                Field.PASSWORD -> copy(fields = fields.copy(passwordErrorKey = null))
                else -> this
            }

        private fun SignUpContract.ProfileState.clear(field: Field) =
            when (field) {
                Field.USERNAME -> copy(usernameErrorKey = null)
                Field.PHONE -> copy(phoneErrorKey = null)
                else -> this
            }

        private fun SignUpContract.State.validate(field: Field) =
            when (field) {
                Field.EMAIL -> copy(fields = fields.copy(emailErrorKey = Validate.email(fields.email)))
                Field.PASSWORD -> copy(fields = fields.copy(passwordErrorKey = Validate.password(fields.password)))
                else -> this
            }

        private fun SignUpContract.ProfileState.validate(field: Field) =
            when (field) {
                Field.USERNAME -> copy(usernameErrorKey = Validate.username(username))
                Field.PHONE -> copy(phoneErrorKey = Validate.phone(phone))
                else -> this
            }

        fun onEvent(event: SignUpContract.Event) {
            when (event) {
                is EmailChanged ->
                    updateState {
                        copy(fields = fields.copy(email = event.email)).clear(
                            Field.EMAIL,
                        )
                    }

                is PasswordChanged ->
                    updateState {
                        copy(fields = fields.copy(password = event.password)).clear(
                            Field.PASSWORD,
                        )
                    }

                is RememberChanged -> updateState { copy(rememberMe = event.isChecked) }

                is EmailBlur -> updateState { validate(Field.EMAIL) }
                is PasswordBlur -> updateState { validate(Field.PASSWORD) }

                is UsernameChanged -> updateProfile { copy(username = event.username).clear(Field.USERNAME) }
                is PhoneChanged -> updateProfile { copy(phone = event.phone).clear(Field.PHONE) }
                is UsernameBlur -> updateProfile { validate(Field.USERNAME) }
                is PhoneBlur -> updateProfile { validate(Field.PHONE) }

                is AvatarPicked -> onAvatarPicked(event.uriString)
                is PickAvatar -> sendEffect(OpenAvatarPicker)
                is RegisterWithGoogle -> sendEffect(OpenGoogleSignIn)

                is SubmitRegister -> submitRegister()
                is ForwardExtended -> submitExtended()
                is NavigateToExtendedRequested -> submitRegister()

                is CancelExtended ->
                    viewModelScope.launch {
                        updateProfile { SignUpContract.ProfileState() }
                        updateState { SignUpContract.State() }
                        sendEffect(BackFromExtended)
                    }

                is ErrorShown -> updateState { copy(errorKey = null) }
            }
        }

        private fun onAvatarPicked(uriString: String) =
            viewModelScope.launch {
                runCatching {
                    val oldPath = _profile.value.avatarPath

                    val newPath =
                        withContext(Dispatchers.IO) {
                            val saved = avatarStorage.saveAvatarFromUri(uriString)
                            avatarStorage.deleteAvatar(oldPath)
                            saved
                        }

                    updateProfile { copy(avatarPath = newPath) }
                }.onFailure {
                    sendEffect(SignUpContract.Effect.ShowMessage(AppText.OtherInfo.UNKNOWN_ERROR))
                }
            }

        private suspend fun registerLocal(normalizedPhone: String) {
            val current = localStorage.userProfile.first()
            val updated =
                (current ?: UserProfile()).copy(
                    username = _profile.value.username,
                    phone = normalizedPhone,
                    avatarPath = _profile.value.avatarPath,
                    isCompleted = true,
                )
            localStorage.saveUserProfile(updated)
        }

        private suspend fun registerRemote(normalizedPhone: String) {
            val email =
                _state.value.fields.email
                    .trim()
            val password = _state.value.fields.password
            val name = _profile.value.username
            val phone = normalizedPhone
            println("REGISTER: email=$email passwordLen=${password.length} name=$name phone=$phone")

            val imageFile =
                _profile.value.avatarPath
                    ?.takeIf { it.isNotBlank() }
                    ?.let { File(it) }
                    ?.takeIf { it.exists() }

            val auth =
                authRepository
                    .register(
                        email = email,
                        password = password,
                    ).getOrElse { throw it }

            authPreferences.saveTokens(auth.accessToken, auth.refreshToken)

            val current = localStorage.userProfile.first()
            val updated =
                (current ?: UserProfile()).copy(
                    username = name,
                    phone = phone,
                    avatarPath = _profile.value.avatarPath,
                    isCompleted = true,
                )
            localStorage.saveUserProfile(updated)
        }

        private fun submitRegister() =
            viewModelScope.launch {
                val email =
                    _state.value.fields.email
                        .trim()
                val password = _state.value.fields.password

                updateState { copy(isLoading = true, errorKey = null) }

                runCatching {
                    val auth = authRepository.register(email, password).getOrElse { throw it }

                    authPreferences.saveTokens(auth.accessToken, auth.refreshToken)
                    authPreferences.saveUserId(auth.user.id)
                }.onSuccess {
                    sendEffect(NavigateToExtended)
                }.onFailure { e ->
                    val msg = e.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
                    sendEffect(SignUpContract.Effect.ShowToast(msg))
                }

                updateState { copy(isLoading = false) }
            }

        private fun submitExtended() =
            viewModelScope.launch {
                val normalizedPhone = _profile.value.phone.replace(Regex("[^+\\d]"), "")

                updateProfile { copy(isLoading = true, errorKey = null) }

                runCatching {
                    val userId = authPreferences.userId.first() ?: throw Exception("UserId missing")
                    val access = authPreferences.accessToken.first() ?: throw Exception("Token missing")

                    authRepository
                        .editUser(
                            userId = userId,
                            accessToken = access,
                            name = _profile.value.username,
                            phone = normalizedPhone,
                        ).getOrElse { throw it }

                    val current = localStorage.userProfile.first()
                    val updated =
                        (current ?: UserProfile()).copy(
                            username = _profile.value.username,
                            phone = normalizedPhone,
                            avatarPath = _profile.value.avatarPath, // локально
                            isCompleted = true,
                        )
                    localStorage.saveUserProfile(updated)
                }.onSuccess {
                    sendEffect(SignUpContract.Effect.NavigateToHome)
                }.onFailure { e ->
                    val msg = e.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
                    sendEffect(SignUpContract.Effect.ShowToast(msg))
                }

                updateProfile { copy(isLoading = false) }
            }
    }
