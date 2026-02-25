package com.spp.android.myapplication.presentation.feature.contacts.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
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
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel
@Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var pendingDeleteJob: Job? = null
    private var lastDeleted: List<Contact> = emptyList()

    private companion object {
        const val USE_PHONEBOOK_CONTACTS = false
    }

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
                    selected = newSelected,
                    isSelectionMode = newSelected.isNotEmpty(),
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
        if (ids.isEmpty()) return

        val toRemove = _state.value.items.filter { ids.contains(it.id) }
        lastDeleted = toRemove

        removeFromUi(ids)

        scheduleFinalizeDelete(ids.toList())

        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorKey = null) }

        runCatching {
            val remote = contactsRepository.getUserContactsRemote()
            val local = contactsRepository.loadContactsLocal()
            val phone =
                if (USE_PHONEBOOK_CONTACTS) contactsRepository.loadContactsPhone() else emptyList()

            val merged = (remote + local + phone).distinctBy { it.id }
            sortContacts(merged)
        }.onSuccess { list ->
            _state.update { it.copy(items = sortContacts(list), isLoading = false) }
        }.onFailure {
            _state.update {
                it.copy(
                    isLoading = false,
                    errorKey = AppText.OtherInfo.UNKNOWN_ERROR,
                )
            }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED))
        }
    }

    private fun delete(item: Contact) {
        lastDeleted = listOf(item)
        removeFromUi(setOf(item.id))

        viewModelScope.launch {
            runCatching {
                runCatching { contactsRepository.deleteUserContactRemote(item.id).getOrThrow() }
                runCatching { contactsRepository.deleteContactLocal(item.id) }
            }.onFailure {
                Log.e("Contacts", "Delete failed", it)
            }
        }

        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED))
    }

    private fun undoDelete() = viewModelScope.launch {
        val restore = lastDeleted
        if (restore.isEmpty()) return@launch

        runCatching {
            restore.forEach { c ->
                runCatching { contactsRepository.addUserContactRemote(c.id).getOrThrow() }
                runCatching { contactsRepository.addContactLocal(c) }
            }
        }.onSuccess {
            load()
            lastDeleted = emptyList()
        }.onFailure {
            Log.e("Contacts", "Undo failed", it)
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private fun sortContacts(list: List<Contact>): List<Contact> = list.sortedWith(
        compareBy<Contact> { it.name.lowercase() }.thenBy { it.subtitle.lowercase() }
            .thenBy { it.id },
    )

    private fun scheduleFinalizeDelete(ids: List<Int>) {
        pendingDeleteJob?.cancel()
        pendingDeleteJob = viewModelScope.launch {
            delay(5_000)

            ids.forEach { id ->
                val remoteRes = runCatching {
                    contactsRepository.deleteUserContactRemote(id).getOrThrow()
                }
                if (remoteRes.isFailure) {
                    Log.e(
                        "Contacts", "Remote delete failed for id=$id", remoteRes.exceptionOrNull()
                    )
                }

                val localRes = runCatching {
                    contactsRepository.deleteContactLocal(id)
                }
                if (localRes.isFailure) {
                    Log.e("Contacts", "Local delete failed for id=$id", localRes.exceptionOrNull())
                }
            }

            lastDeleted = emptyList()
            pendingDeleteJob = null
        }
    }

    private fun removeFromUi(ids: Set<Int>) {
        _state.update { st ->
            st.copy(
                items = sortContacts(st.items.filterNot { ids.contains(it.id) }),
                selected = emptySet(),
                isSelectionMode = false,
            )
        }
    }
}
