package com.spp.android.myapplication.screens.contacts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel() {

    private val _contacts = MutableStateFlow(generateContacts())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

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