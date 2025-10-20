package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event
import com.spp.android.myapplication.presentation.texts.AppText
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
class EditProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(EditProfileContract.State())
    val state: StateFlow<EditProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Event.Load)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.Load -> load()
            is Event.UsernameChanged -> _state.update {
                it.copy(username = event.usernameChanged, usernameErrorKey = null)
            }

            is Event.CareerChanged -> _state.update { it.copy(career = event.careerChanged) }
            is Event.PhoneChanged -> _state.update {
                it.copy(phone = event.phoneChanged, phoneErrorKey = null)
            }

            is Event.AddressChanged -> _state.update { it.copy(address = event.addressChanged) }
            is Event.BirthdateChanged -> _state.update { it.copy(birthdate = event.birthdateChanged) }
            is Event.SaveClicked -> save()
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.AvatarClicked -> sendEffect(Effect.OpenAvatarPicker)
            is Event.ErrorShown -> _state.update { it.copy(errorKey = null) }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorKey = null) }
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
        }.onFailure {
            _state.update {
                it.copy(isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR)
            }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.FAILED_TO_LOAD_PROFILE))
        }
    }

    private fun save() = viewModelScope.launch {
        _state.update { it.copy(isSaving = true, errorKey = null) }

        runCatching { true }.onSuccess {
            _state.update { it.copy(isSaving = false) }
            sendEffect(Effect.Saved)
        }.onFailure {
            _state.update {
                it.copy(isSaving = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR)
            }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.FAILED_PROFILE_SAVE))
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private data class StubProfile(
        val username: String = "",
        val career: String = "",
        val phone: String = "",
        val address: String = "",
        val birthdate: String = ""
    )

    fun onFieldChange(field: String, value: String) {
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
