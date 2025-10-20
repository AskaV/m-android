package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.texts.TextKey

object ContactsContract {

    @Immutable
    data class State(
        val items: List<ContactUi> = emptyList(),
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
        data class ContactClicked(val contactClicked: ContactUi) : Event
        data class DeleteClicked(val deleteClicked: ContactUi) : Event
        data object ErrorShown : Event
        data class ContactLongClicked(val contactLongClicked: ContactUi) : Event
        data class ContactSelectionToggled(val contactSelectionToggled: ContactUi) : Event
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