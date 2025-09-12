package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.compose.runtime.Immutable

object AddContactProfileContract {
    @Immutable
    data class State(
        val id: String = "",
        val name: String = "",
        val linePrimary: String = "",
        val lineSecondary: String = "",
        val isInMyContacts: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data class Load(val id: String) : Event
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