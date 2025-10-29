package com.spp.android.myapplication.presentation.feature.contacts.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.AddContactsClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.BulkDeleteClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactLongClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ContactSelectionToggled
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.DeleteClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.ExitSelectionMode
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.Load
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.SearchClicked
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.UndoDelete
import com.spp.android.myapplication.presentation.feature.contacts.util.PhoneContactsReader
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var lastDeleted: List<ContactUi> = emptyList()

    init {

        viewModelScope.launch {
            repository.observeContacts().collect { list ->
                _state.update { it.copy(items = list) }
            }
        }
        onEvent(Load)
    }

    fun onEvent(event: ContactsContract.Event) {
        when (event) {
            is Load -> load()
            is BackClicked -> sendEffect(Effect.NavigateBack)
            is SearchClicked -> sendEffect(Effect.OpenSearch)
            is AddContactsClicked -> sendEffect(Effect.OpenAddContact)
            is ContactClicked -> sendEffect(Effect.OpenContactProfile(event.contactClicked.id))
            is DeleteClicked -> delete(event.deleteClicked)
            is ErrorShown -> _state.update { it.copy(errorKey = null) }
            is ContactLongClicked -> toggleSelectionMode(event.contactLongClicked.id)
            is ContactSelectionToggled -> toggleSelection(event.contactSelectionToggled.id)
            is BulkDeleteClicked -> bulkDelete()
            is ExitSelectionMode -> _state.update {
                it.copy(selected = emptySet(), isSelectionMode = false)
            }

            is UndoDelete -> undoDelete()
        }
    }

    private fun toggleSelectionMode(contactId: Int) {
        _state.update { st ->
            if (st.isSelectionMode) {
                val newSelected = st.selected.toMutableSet().apply {
                    if (contains(contactId)) remove(contactId) else add(contactId)
                }
                st.copy(
                    selected = newSelected, isSelectionMode = newSelected.isNotEmpty()
                )
            } else {
                st.copy(selected = setOf(contactId), isSelectionMode = true)
            }
        }
    }

    private fun toggleSelection(contactId: Int) {
        _state.update { st ->
            val newSelected = st.selected.toMutableSet().apply {
                if (contains(contactId)) remove(contactId) else add(contactId)
            }
            st.copy(selected = newSelected, isSelectionMode = newSelected.isNotEmpty())
        }
    }

    private fun bulkDelete() {
        val ids = _state.value.selected
        val toRemove = _state.value.items.filter { ids.contains(it.id) }
        lastDeleted = toRemove

        _state.update { st ->
            st.copy(
                items = st.items.filterNot { ids.contains(it.id) },
                selected = emptySet(),
                isSelectionMode = false
            )
        }
        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorKey = null) }
        runCatching {
            repository.importFromSystem()
        }.onSuccess { list ->
            _state.update { it.copy(items = list, isLoading = false) }
        }.onFailure {
            _state.update {
                it.copy(isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR)
            }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED))
        }
    }

    private fun delete(item: ContactUi) = viewModelScope.launch {
        lastDeleted = listOf(item)

        _state.update { it.copy(items = it.items.filterNot { c -> c.id == item.id }) }
        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun undoDelete() {
        if (lastDeleted.isEmpty()) return
        _state.update { st ->
            val restored = (st.items + lastDeleted).sortedBy { it.id }
            st.copy(items = restored)
        }
        lastDeleted = emptyList()
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}