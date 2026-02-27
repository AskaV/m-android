package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.domain.model.Contact

object AddContactsContract {
    @Immutable
    data class State(
        val items: List<Contact> = emptyList(),
        val selected: Set<Int> = emptySet(),
        val isLoading: Boolean = false,
        val error: String = "",
        val isSelectionMode: Boolean = false,
        val isSearchOpen: Boolean = false,
        val query: String = "",
    )

    sealed interface Event {
        data object Load : Event
        data object BackClicked : Event
        data object SearchClicked : Event
        data object MassAddClicked : Event
        data class AddClicked(val dddClicked: Contact) : Event
        data object ErrorShown : Event
        data class UserLongClicked(val contact: Contact) : Event
        data class UserClicked(val contact: Contact) : Event
        data object ExitSelectionMode : Event
        data object SearchClosed : Event
        data class QueryChanged(val query: String) : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowMessage(val message: String) : Effect
    }
}
