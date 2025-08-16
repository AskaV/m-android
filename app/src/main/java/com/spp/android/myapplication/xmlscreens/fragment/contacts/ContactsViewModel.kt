package com.spp.android.myapplication.xmlscreens.fragment.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    init {
        _contacts.value = generateContacts()
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

    fun setContacts(list: List<Contact>) {
        _contacts.value = list
    }
}