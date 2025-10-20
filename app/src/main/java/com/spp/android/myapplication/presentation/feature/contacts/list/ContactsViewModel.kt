package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.*
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
class ContactsViewModel @Inject constructor(private val repository: ContactsRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
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
            repository.loadContacts().ifEmpty { demoUsers() }
        }.onSuccess { list ->
            _state.update { it.copy(items = list, isLoading = false) }
        }.onFailure {
            _state.update { it.copy(isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR) }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED))
        }
    }

    private fun delete(item: ContactUi) = viewModelScope.launch {
        _state.update { it.copy(items = it.items.filterNot { c -> c.id == item.id }) }
        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}