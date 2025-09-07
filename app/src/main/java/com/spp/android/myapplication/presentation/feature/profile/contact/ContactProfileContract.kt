package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.compose.runtime.Immutable

object ContactProfileContract {

    @Immutable
    data class State(
        val contactId: String = "",
        val name: String = "",
        val linePrimary: String = "",
        val lineSecondary: String = "",
        val hasSocial: Boolean = true,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data class Load(val contactId: String) : Event
        data object BackClicked : Event
        data object MessageClicked : Event
        data object AddClicked : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class OpenChat(val contactId: String) : Effect
        data class ShowMessage(val message: String) : Effect
    }
}