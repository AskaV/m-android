package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi

object ContactsContract {

    @Immutable
    data class State(
        val items: List<ContactUi> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = "",
        val selected: Set<String> = emptySet(),
        val isSelectionMode: Boolean = false
    )

    sealed interface Event {
        data object Load : Event
        data object BackClicked : Event
        data object SearchClicked : Event
        data object AddContactsClicked : Event
        data class ContactClicked(val item: ContactUi) : Event
        data class DeleteClicked(val item: ContactUi) : Event
        data object ErrorShown : Event

        data class ContactLongClicked(val item: ContactUi) : Event
        data class ContactSelectionToggled(val item: ContactUi) : Event
        data object BulkDeleteClicked : Event
        data object ExitSelectionMode : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object OpenSearch : Effect
        data object OpenAddContacts : Effect
        data class OpenContactProfile(val contactId: String) : Effect
        data class ShowMessage(val message: String) : Effect
    }
}