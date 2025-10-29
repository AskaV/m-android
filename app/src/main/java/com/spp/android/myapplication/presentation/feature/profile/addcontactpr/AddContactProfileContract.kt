package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.compose.runtime.Immutable

object AddContactProfileContract {
    @Immutable
    data class State(
        val id: Int = 0,
        val name: String = "",
        val linePrimary: String = "",
        val lineSecondary: String = "",
        val isInMyContacts: Boolean = false,
        val isLoading: Boolean = false,
        val error: String = "",
        val avatarUrl: String? = null

    )

    sealed interface Event {
        data class Load(val id: Int) : Event
        data object BackClicked : Event
        data object MessageClicked : Event
        data object AddToContactsClicked : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowMessage(val message: String) : Effect
    }
}