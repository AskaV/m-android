package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.compose.runtime.Immutable

object EditProfileContract {

    @Immutable
    data class State(
        val username: String = "",
        val career: String = "",
        val phone: String = "",
        val address: String = "",
        val birthdate: String = "",
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val error: String? = null,
        val usernameError: String? = null,
        val phoneError: String? = null
    )

    sealed interface Event {
        data object Load : Event
        data class UsernameChanged(val value: String) : Event
        data class CareerChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data class AddressChanged(val value: String) : Event
        data class BirthdateChanged(val value: String) : Event
        data object SaveClicked : Event
        data object BackClicked : Event
        data object AvatarClicked : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object OpenAvatarPicker : Effect
        data class ShowMessage(val message: String) : Effect
        data object Saved : Effect
    }
}