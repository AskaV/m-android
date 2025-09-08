package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileContract.State())
    val state: StateFlow<EditProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<EditProfileContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(EditProfileContract.Event.Load)
    }

    fun onEvent(event: EditProfileContract.Event) {
        when (event) {
            EditProfileContract.Event.Load -> load()
            is EditProfileContract.Event.UsernameChanged ->
                _state.update { it.copy(username = event.value, usernameError = null) }

            is EditProfileContract.Event.CareerChanged ->
                _state.update { it.copy(career = event.value) }

            is EditProfileContract.Event.PhoneChanged ->
                _state.update { it.copy(phone = event.value, phoneError = null) }

            is EditProfileContract.Event.AddressChanged ->
                _state.update { it.copy(address = event.value) }

            is EditProfileContract.Event.BirthdateChanged ->
                _state.update { it.copy(birthdate = event.value) }

            EditProfileContract.Event.SaveClicked -> save()
            EditProfileContract.Event.BackClicked -> emit(EditProfileContract.Effect.NavigateBack)
            EditProfileContract.Event.AvatarClicked -> emit(EditProfileContract.Effect.OpenAvatarPicker)
            EditProfileContract.Event.ErrorShown ->
                _state.update { it.copy(error = null) }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching {
            StubProfile(
                username = "Lucile Alvarado",
                career = "Product Designer",
                phone = "+1 234 567 890",
                address = "New York, USA",
                birthdate = "1996-07-11"
            )
        }.onSuccess { p ->
            _state.update {
                it.copy(
                    username = p.username,
                    career = p.career,
                    phone = p.phone,
                    address = p.address,
                    birthdate = p.birthdate,
                    isLoading = false
                )
            }
        }.onFailure { t ->
            _state.update { it.copy(isLoading = false, error = t.message ?: "Unknown error") }
            emit(EditProfileContract.Effect.ShowMessage("Failed to load profile"))
        }
    }

    private fun save() = viewModelScope.launch {
        val s = _state.value
        val nameErr = if (s.username.isBlank()) "Name is required" else null
        val phoneErr = if (s.phone.isBlank()) "Phone is required" else null
        if (nameErr != null || phoneErr != null) {
            _state.update { it.copy(usernameError = nameErr, phoneError = phoneErr) }
            return@launch
        }

        _state.update { it.copy(isSaving = true, error = null) }
        runCatching {
            true
        }.onSuccess {
            _state.update { it.copy(isSaving = false) }
            emit(EditProfileContract.Effect.Saved)
        }.onFailure { t ->
            _state.update { it.copy(isSaving = false, error = t.message ?: "Unknown error") }
            emit(EditProfileContract.Effect.ShowMessage("Failed to save profile"))
        }
    }

    private fun emit(effect: EditProfileContract.Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private data class StubProfile(
        val username: String,
        val career: String,
        val phone: String,
        val address: String,
        val birthdate: String
    )
}