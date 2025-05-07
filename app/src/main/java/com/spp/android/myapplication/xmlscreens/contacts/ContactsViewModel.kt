package com.spp.android.myapplication.xmlscreens.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spp.android.myapplication.R

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    init {
        _contacts.value = listOf(
            Contact("Ava Smith", "Photograph", R.drawable.profile_avatar),
            Contact("Jessie Brown", "Actress", R.drawable.profile_avatar),
            Contact("Jackie Taylor", "Financier", R.drawable.profile_avatar),
            Contact("Jenny Walker", "Make-up artist", R.drawable.profile_avatar),
            Contact("Freddy Harris", "Secretary", R.drawable.profile_avatar),
            Contact("Annie King", "Nurse", R.drawable.profile_avatar),
            Contact("Ava Smith", "Photograph", R.drawable.profile_avatar),
            Contact("Jessie Brown", "Actress", R.drawable.profile_avatar),
            Contact("Jackie Taylor", "Financier", R.drawable.profile_avatar),
            Contact("Jenny Walker", "Make-up artist", R.drawable.profile_avatar),
            Contact("Freddy Harris", "Secretary", R.drawable.profile_avatar),
            Contact("Annie King", "Nurse", R.drawable.profile_avatar)
        )
    }
}