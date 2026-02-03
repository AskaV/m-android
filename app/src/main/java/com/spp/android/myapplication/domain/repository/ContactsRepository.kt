package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.domain.model.Contact

interface ContactsRepository {
    suspend fun loadContacts(): List<Contact>

    suspend fun addContact(contact: Contact)

    suspend fun deleteContact(contactId: Int): Boolean

    suspend fun setContactAvatar(
        contactId: Int,
        avatarUrl: String?,
    )
}
