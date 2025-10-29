package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.presentation.texts.TextKey

object ContactsContract {

    @Immutable
    data class State(
        val items: List<Contact> = emptyList(),
        val isLoading: Boolean = false,
        val errorKey: TextKey? = null,
        val selected: Set<Int> = emptySet(),
        val isSelectionMode: Boolean = false
    )

    sealed interface Event {
        data object Load : Event
        data object BackClicked : Event
        data object SearchClicked : Event
        data object AddContactsClicked : Event
        data class ContactClicked(val contactClicked: Contact) : Event
        data class DeleteClicked(val deleteClicked: Contact) : Event
        data object ErrorShown : Event
        data class ContactLongClicked(val contactLongClicked: Contact) : Event
        data class ContactSelectionToggled(val contactSelectionToggled: Contact) : Event
        data object BulkDeleteClicked : Event
        data object ExitSelectionMode : Event
        data object UndoDelete : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object OpenSearch : Effect
        data object OpenAddContact : Effect
        data class OpenContactProfile(val contactId: Int) : Effect
        data class ShowMessage(val messageKey: TextKey) : Effect
    }
}