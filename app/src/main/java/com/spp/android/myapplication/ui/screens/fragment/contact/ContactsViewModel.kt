package com.spp.android.myapplication.ui.screens.fragment.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() { //todo place near Composable

    private val _selected = MutableStateFlow<Set<Contact>>(emptySet())
    val selected: StateFlow<Set<Contact>> = _selected.asStateFlow()

    val isSelectionMode: StateFlow<Boolean> = _selected
            .map { it.isNotEmpty() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _contacts = MutableStateFlow(generateContacts())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private val deletedContacts = mutableListOf<Contact>()

    private var lastBatchDeleted: List<Contact> = emptyList()


    fun deleteContact(contact: Contact) {
        deletedContacts.add(contact)
        _contacts.value -= contact
    }

    fun undoDelete() {
        if (deletedContacts.isNotEmpty()) {
            val lastDeleted = deletedContacts.removeAt(deletedContacts.lastIndex)
            _contacts.value = listOf(lastDeleted) + _contacts.value
        }
    }

    fun addContact(contact: Contact) {
        _contacts.value = listOf(contact) + _contacts.value
    }

    private fun generateContacts(): List<Contact> {
        return listOf(
            Contact("Ava Smith", "Photograph", generateAvatarUrl()),
            Contact("Jessie Brown", "Actress", generateAvatarUrl()),
            Contact("Jackie Taylor", "Financier", generateAvatarUrl()),
            Contact("Jenny Walker", "Make-up artist", generateAvatarUrl()),
            Contact("Freddy Harris", "Secretary", generateAvatarUrl()),
            Contact("Annie King", "Nurse", generateAvatarUrl()),
            Contact("Ava Smith", "Photograph", generateAvatarUrl()),
            Contact("Jessie Brown", "Actress", generateAvatarUrl()),
            Contact("Jackie Taylor", "Financier", generateAvatarUrl()),
            Contact("Jenny Walker", "Make-up artist", generateAvatarUrl()),
            Contact("Freddy Harris", "Secretary", generateAvatarUrl()),
            Contact("Annie King", "Nurse", generateAvatarUrl())
        )
    }

    private fun generateAvatarUrl(): String {
        val id = (1..70).random()
        return "https://i.pravatar.cc/150?img=$id"
    }

    fun startSelection(with: Contact) {
        _selected.value = setOf(with)
    }

    fun toggleSelection(contact: Contact) {
        _selected.value = _selected.value.toMutableSet().also { set ->
            if (!set.add(contact)) set.remove(contact)
        }
    }

    private fun clearSelection() {
        _selected.value = emptySet()
    }

    fun deleteSelected() {
        val toDelete = _selected.value.toList()
        if (toDelete.isEmpty()) return

        lastBatchDeleted = toDelete.toList()
        deletedContacts.addAll(toDelete)

        _contacts.value -= toDelete.toSet()
        clearSelection()
    }

    fun undoLastBatchDelete() {
        if (lastBatchDeleted.isEmpty()) return
        _contacts.value = lastBatchDeleted + _contacts.value
        deletedContacts.removeAll(lastBatchDeleted.toSet())
        lastBatchDeleted = emptyList()
    }
}