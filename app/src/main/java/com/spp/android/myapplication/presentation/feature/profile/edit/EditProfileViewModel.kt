package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.domain.model.UserProfile
import com.spp.android.myapplication.domain.repository.AuthRepository
import com.spp.android.myapplication.domain.storage.AvatarStorage
import com.spp.android.myapplication.domain.storage.LocalStorage
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel
    @Inject
    constructor(
        private val localStorage: LocalStorage,
        private val avatarStorage: AvatarStorage,
        private val authRepository: AuthRepository,
        private val authPreferences: AuthPreferences,
    ) : ViewModel() {
        private enum class StorageMode { LOCAL, REMOTE, BOTH }

        private val storageMode = StorageMode.BOTH

        private val _state = MutableStateFlow(EditProfileContract.State())
        val state: StateFlow<EditProfileContract.State> = _state.asStateFlow()

        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect = _effect.receiveAsFlow()

        private var cachedProfile: UserProfile? = null

        init {
            onEvent(Event.Load)
        }

        fun onEvent(event: Event) {
            when (event) {
                is Event.Load -> load()

                is Event.UsernameChanged ->
                    _state.update {
                        it.copy(
                            username = event.usernameChanged,
                            usernameErrorKey = null,
                        )
                    }

                is Event.CareerChanged -> _state.update { it.copy(career = event.careerChanged) }

                is Event.PhoneChanged ->
                    _state.update {
                        it.copy(
                            phone = event.phoneChanged,
                            phoneErrorKey = null,
                        )
                    }

                is Event.AddressChanged -> _state.update { it.copy(address = event.addressChanged) }

                is Event.BirthdateChanged -> _state.update { it.copy(birthdate = event.birthdateChanged) }

                is Event.SaveClicked -> save()
                is Event.BackClicked -> sendEffect(Effect.NavigateBack)

                is Event.AvatarClicked -> sendEffect(Effect.OpenAvatarPicker)

                is Event.AvatarSelected -> onAvatarSelected(event.uriString)
                is Event.AvatarDeleted -> onAvatarDeleted()

                is Event.ErrorShown -> _state.update { it.copy(errorKey = null) }
            }
        }

        private fun load() =
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorKey = null) }

                localStorage.userProfile.collect { profile ->
                    cachedProfile = profile
                    if (profile != null) {
                        _state.update {
                            it.copy(
                                username = profile.username,
                                career = profile.career,
                                phone = profile.phone,
                                address = profile.address,
                                birthdate = profile.birthdate,
                                avatarPath = profile.avatarPath,
                                isLoading = false,
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }

        private fun onAvatarSelected(uriString: String) =
            viewModelScope.launch {
                runCatching {
                    val newPath = avatarStorage.saveAvatarFromUri(uriString)

                    val current = cachedProfile
                    if (current != null) {
                        avatarStorage.deleteAvatar(current.avatarPath)

                        val updated = current.copy(avatarPath = newPath)
                        localStorage.saveUserProfile(updated)
                        cachedProfile = updated
                    }

                    _state.update { it.copy(avatarPath = newPath) }
                }.onFailure {
                    sendEffect(Effect.ShowMessage(AppText.OtherInfo.UNKNOWN_ERROR))
                }
            }

        private fun onAvatarDeleted() =
            viewModelScope.launch {
                val current = cachedProfile ?: return@launch

                runCatching {
                    avatarStorage.deleteAvatar(current.avatarPath)

                    val updated = current.copy(avatarPath = null)
                    localStorage.saveUserProfile(updated)
                    cachedProfile = updated

                    _state.update { it.copy(avatarPath = null) }
                }.onFailure {
                    sendEffect(Effect.ShowMessage(AppText.OtherInfo.UNKNOWN_ERROR))
                }
            }

        private fun save() =
            viewModelScope.launch {
                _state.update { it.copy(isSaving = true, errorKey = null) }

                runCatching {
                    val current = cachedProfile
                    val updated =
                        if (current != null) {
                            current.copy(
                                username = state.value.username,
                                career = state.value.career,
                                phone = state.value.phone,
                                address = state.value.address,
                                birthdate = state.value.birthdate,
                                avatarPath = state.value.avatarPath,
                            )
                        } else {
                            UserProfile(
                                username = state.value.username,
                                career = state.value.career,
                                phone = state.value.phone,
                                address = state.value.address,
                                birthdate = state.value.birthdate,
                                avatarPath = state.value.avatarPath,
                            )
                        }

                    if (storageMode == StorageMode.REMOTE || storageMode == StorageMode.BOTH) {
                        val userId = authPreferences.userId.first() ?: throw Exception("UserId missing")
                        val token = authPreferences.accessToken.first()
                        if (token.isBlank()) throw Exception("Token missing")

                        println(
                            "EDIT PUT: name=${updated.username} phone=${updated.phone} address=${updated.address} career=${updated.career} birth=${updated.birthdate}",
                        )

                        authRepository
                            .editUser(
                                userId = userId,
                                accessToken = token,
                                name = updated.username.ifBlank { null },
                                phone = updated.phone.ifBlank { null },
                                address = updated.address.ifBlank { null },
                                career = updated.career.ifBlank { null },
                                birthday = updated.birthdate.ifBlank { null },
                                facebook = null,
                                instagram = null,
                                twitter = null,
                                linkedin = null,
                            ).getOrElse { throw it }
                    }

                    if (storageMode == StorageMode.LOCAL || storageMode == StorageMode.BOTH) {
                        localStorage.saveUserProfile(updated)
                        cachedProfile = updated
                    }

                    true
                }.onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    sendEffect(Effect.Saved)
                }.onFailure { e ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            errorKey = AppText.OtherInfo.UNKNOWN_ERROR,
                        )
                    }
                    sendEffect(Effect.ShowMessage(AppText.OtherInfo.FAILED_PROFILE_SAVE))
                }
            }

        private fun sendEffect(effect: Effect) =
            viewModelScope.launch {
                _effect.send(effect)
            }

        fun onFieldChange(
            field: String,
            value: String,
        ) {
            _state.update {
                when (field) {
                    "username" -> it.copy(username = value)
                    "career" -> it.copy(career = value)
                    "phone" -> it.copy(phone = value)
                    "address" -> it.copy(address = value)
                    "birthdate" -> it.copy(birthdate = value)
                    else -> it
                }
            }
        }
    }
