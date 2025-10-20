package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.compose.runtime.Immutable

object AddContactContract {

    @Immutable
    data class State(
        val username: String = "",
        val career: String = "",
        val email: String = "",
        val phone: String = "",
        val address: String = "",
        val dateOfBirth: String = "",

        val usernameError: String = "",
        val careerError: String = "",
        val emailError: String = "",
        val phoneError: String = "",
        val addressError: String = "",
        val dateOfBirthError: String = "",

        val isSaving: Boolean = false
    )

    sealed interface Event {
        data object BackClicked : Event
        data object AvatarClicked : Event
        data object SaveClicked : Event

        data class UsernameChanged(val value: String) : Event
        data class CareerChanged(val value: String) : Event
        data class EmailChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data class AddressChanged(val value: String) : Event
        data class DateOfBirthChanged(val value: String) : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowMessage(val message: String) : Effect
    }
}