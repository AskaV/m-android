package com.spp.android.myapplication.presentation.feature.contacts.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var pendingDeleteJob: Job? = null
    private var lastDeleted: List<Contact> = emptyList()

    private var apiIdsSnapshot: Set<Int> = emptySet()
    private var localIdsSnapshot: Set<Int> = emptySet()
    private var phonebookIdsSnapshot: Set<Int> = emptySet()

    private val phonebookFlow = MutableStateFlow<List<Contact>>(emptyList())
    private var baseList: List<Contact> = emptyList()

    init {
        observeMergedContacts()
        onEvent(Event.Load)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.Load -> refresh()

            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.AddContactsClicked -> sendEffect(Effect.OpenAddContact)

            is Event.ContactClicked -> sendEffect(Effect.OpenContactProfile(event.contactClicked.id))

            is Event.DeleteClicked -> delete(listOf(event.deleteClicked))

            is Event.BulkDeleteClicked -> {
                val ids = _state.value.selected
                if (ids.isEmpty()) return
                val items = _state.value.items.filter { it.id in ids }
                delete(items)
            }

            is Event.UndoDelete -> undoDelete()
            is Event.ErrorShown -> _state.update { it.copy(errorKey = null) }

            is Event.ContactLongClicked -> toggleSelectionMode(event.contactLongClicked.id)
            is Event.ContactSelectionToggled -> toggleSelection(event.contactSelectionToggled.id)

            is Event.ExitSelectionMode -> _state.update {
                it.copy(selected = emptySet(), isSelectionMode = false)
            }

            is Event.SearchClicked -> _state.update { it.copy(isSearchOpen = true) }

            is Event.SearchClosed -> _state.update { st ->
                st.copy(
                    isSearchOpen = false,
                    query = "",
                    items = baseList,
                    selected = st.selected.intersect(baseList.map { it.id }.toSet()),
                    isSelectionMode = st.selected.isNotEmpty(),
                )
            }

            is Event.QueryChanged -> _state.update { st ->
                val q = event.query
                val visible = applyQuery(baseList, q)
                val ids = visible.map { it.id }.toSet()

                st.copy(
                    query = q,
                    items = visible,
                    selected = st.selected.intersect(ids),
                    isSelectionMode = st.isSelectionMode && st.selected.intersect(ids).isNotEmpty(),
                )
            }
        }
    }

    private fun observeMergedContacts() = viewModelScope.launch {
        combine(
            contactsRepository.apiMyContacts,
            contactsRepository.localAdded,
            phonebookFlow,
        ) { apiMine, localAdded, phonebook ->
            apiIdsSnapshot = apiMine.map { it.id }.toSet()
            localIdsSnapshot = localAdded.map { it.id }.toSet()
            phonebookIdsSnapshot = phonebook.map { it.id }.toSet()

            baseList = sortContacts((apiMine + localAdded + phonebook).distinctBy { it.id })

            applyQuery(baseList, _state.value.query)
        }.collect { visible ->
            _state.update { st ->
                val existingIds = visible.map { it.id }.toSet()
                val filteredSelected = st.selected.intersect(existingIds)

                st.copy(
                    items = visible,
                    selected = filteredSelected,
                    isSelectionMode = filteredSelected.isNotEmpty(),
                )
            }
        }
    }

    private fun applyQuery(list: List<Contact>, query: String): List<Contact> {
        val q = query.trim()
        if (q.isEmpty()) return list
        val lower = q.lowercase()
        return list.filter { c ->
            c.name.lowercase().contains(lower) || c.subtitle.lowercase().contains(lower)
        }
    }

    private fun refresh() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorKey = null) }

        runCatching { contactsRepository.loadContactsPhone() }.onSuccess {
            phonebookFlow.value = it
        }.onFailure { Log.e("Contacts", "Phonebook load failed", it) }

        contactsRepository.refreshMyContacts()
            .onSuccess { _state.update { it.copy(isLoading = false) } }.onFailure {
                _state.update {
                    it.copy(
                        isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR
                    )
                }
                sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED))
            }
    }

    private fun delete(items: List<Contact>) {
        if (items.isEmpty()) return
        lastDeleted = items

        viewModelScope.launch {
            items.forEach { c ->
                runCatching {
                    when {
                        phonebookIdsSnapshot.contains(c.id) -> {}
                        apiIdsSnapshot.contains(c.id) -> contactsRepository.deleteContactOfflineFirst(
                            c.id
                        ).getOrThrow()

                        localIdsSnapshot.contains(c.id) -> contactsRepository.deleteContactLocal(c.id)
                        else -> {
                            contactsRepository.deleteContactLocal(c.id)
                            contactsRepository.deleteContactOfflineFirst(c.id)
                        }
                    }
                }.onFailure { Log.e("Contacts", "Immediate delete failed id=${c.id}", it) }
            }
        }

        removeFromUi(items.map { it.id }.toSet())
        scheduleFinalizeDelete()
        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun undoDelete() = viewModelScope.launch {
        val restore = lastDeleted
        if (restore.isEmpty()) return@launch

        pendingDeleteJob?.cancel()
        pendingDeleteJob = null

        restore.forEach { c ->
            runCatching {
                when {
                    phonebookIdsSnapshot.contains(c.id) -> {}
                    localIdsSnapshot.contains(c.id) -> contactsRepository.addContactLocal(c)
                    else -> contactsRepository.addContactOfflineFirst(c).getOrThrow()
                }
            }.onFailure { Log.e("Contacts", "Undo restore failed", it) }
        }

        lastDeleted = emptyList()
    }

    private fun scheduleFinalizeDelete() {
        pendingDeleteJob?.cancel()
        pendingDeleteJob = viewModelScope.launch {
            delay(5_000)
            lastDeleted = emptyList()
            pendingDeleteJob = null
        }
    }

    private fun toggleSelectionMode(contactId: Int) {
        _state.update { st ->
            if (st.isSelectionMode) {
                val newSelected = st.selected.toMutableSet().apply {
                    if (contains(contactId)) remove(contactId) else add(contactId)
                }
                st.copy(selected = newSelected, isSelectionMode = newSelected.isNotEmpty())
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

    private fun removeFromUi(ids: Set<Int>) {
        _state.update { st ->
            st.copy(
                items = sortContacts(st.items.filterNot { it.id in ids }),
                selected = emptySet(),
                isSelectionMode = false,
            )
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch { _effect.send(effect) }

    private fun sortContacts(list: List<Contact>): List<Contact> = list.sortedWith(
        compareBy<Contact> { it.name.lowercase() }.thenBy { it.subtitle.lowercase() }
            .thenBy { it.id },
    )
}