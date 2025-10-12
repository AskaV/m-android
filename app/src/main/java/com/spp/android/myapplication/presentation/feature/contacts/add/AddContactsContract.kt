package com.spp.android.myapplication.presentation.feature.contacts.add

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi

object AddContactsContract {
    @Immutable
    data class State(
        val items: List<ContactUi> = emptyList(),
        val selected: Set<String> = emptySet(),
        val isLoading: Boolean = false,
        val error: String = ""
    )

    sealed interface Event {
        data object Load : Event
        data object BackClicked : Event
        data object SearchClicked : Event
        data class ToggleSelect(val item: ContactUi) : Event
        data object MassAddClicked : Event
        data class AddClicked(val item: ContactUi) : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object OpenSearch : Effect
        data class ShowMessage(val message: String) : Effect
    }
}