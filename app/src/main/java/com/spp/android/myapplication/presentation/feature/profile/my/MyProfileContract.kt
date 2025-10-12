package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.compose.runtime.Immutable

object MyProfileContract {

    @Immutable
    data class State(
        val name: String = "",
        val linePrimary: String = "",
        val lineSecondary: String = "",
        val isCompleted: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = ""
    )

    sealed interface Event {
        data object Load : Event
        data object Refresh : Event
        data object EditProfileClicked : Event
        data object ViewContactsClicked : Event
        data object LogoutClicked : Event
        data object ErrorShown : Event
        data object MarkCompleted : Event

        data object SignUpFinished : Event
        data class ProfileSaved(
            val username: String,
            val career: String,
            val phone: String,
            val address: String,
            val birthdate: String
        ) : Event
    }

    sealed interface Effect {
        data class ShowMessage(val message: String) : Effect
        data object NavigateToEditProfile : Effect
        data object NavigateToContacts : Effect
        data object NavigateToAuth : Effect
    }
}