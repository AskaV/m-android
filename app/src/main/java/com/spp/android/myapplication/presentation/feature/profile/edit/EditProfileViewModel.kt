package com.spp.android.myapplication.presentation.feature.profile.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileContract.Event
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileContract.State())
    val state: StateFlow<EditProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Event.Load)
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.Load -> load()
            is Event.UsernameChanged -> _state.update {
                it.copy(username = event.value, usernameError = null)
            }

            is Event.CareerChanged -> _state.update { it.copy(career = event.value) }

            is Event.PhoneChanged -> _state.update {
                it.copy(phone = event.value, phoneError = null)
            }

            is Event.AddressChanged -> _state.update { it.copy(address = event.value) }

            is Event.BirthdateChanged -> _state.update { it.copy(birthdate = event.value) }

            is Event.SaveClicked -> save()
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.AvatarClicked -> sendEffect(Effect.OpenAvatarPicker)
            is Event.ErrorShown -> _state.update { it.copy(error = null) }
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
            _state.update {
                it.copy(
                    isLoading = false,
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
                )
            }
            sendEffect(
                Effect.ShowMessage(
                    AppText.OtherInfo.FAILED_TO_LOAD_PROFILE.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun save() = viewModelScope.launch {
        _state.update { it.copy(isSaving = true, error = null) }
        runCatching {
            true
        }.onSuccess {
            _state.update { it.copy(isSaving = false) }
            sendEffect(Effect.Saved)
        }.onFailure { t ->
            _state.update {
                it.copy(
                    isSaving = false,
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
                )
            }
            sendEffect(
                Effect.ShowMessage(
                    AppText.OtherInfo.FAILED_PROFILE_SAVE.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
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