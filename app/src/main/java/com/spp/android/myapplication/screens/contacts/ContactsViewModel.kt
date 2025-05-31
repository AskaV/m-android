package com.spp.android.myapplication.screens.contacts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel() {

    private val _contacts = MutableStateFlow(generateContacts())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()
    private val deletedContacts = mutableListOf<Contact>()


    fun deleteContact(contact: Contact) {
        deletedContacts.add(contact)
        _contacts.value = _contacts.value - contact
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

    fun setContacts(list: List<Contact>) {
        _contacts.value = list
    }

    companion object {
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
    }
}