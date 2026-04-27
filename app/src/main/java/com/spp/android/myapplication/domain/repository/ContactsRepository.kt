package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    val apiAllUsers: Flow<List<Contact>>
    val apiMyContacts: Flow<List<Contact>>
    val localAdded: Flow<List<Contact>>

    suspend fun refreshAllUsers(): Result<Unit>
    suspend fun refreshMyContacts(): Result<Unit>

    // phonebook
    suspend fun loadContactsPhone(): List<Contact>

    val showPhonebook: Flow<Boolean>
    suspend fun setShowPhonebook(value: Boolean)

    val visibleContacts: Flow<List<Contact>>
    suspend fun refreshVisibleContacts(): Result<Unit>

    suspend fun loadContactsLocal(): List<Contact>
    suspend fun addContactLocal(contact: Contact)
    suspend fun deleteContactLocal(contactId: Int): Boolean

    suspend fun addContactOfflineFirst(contact: Contact): Result<Unit>
    suspend fun deleteContactOfflineFirst(contactId: Int): Result<Unit>

    // phonebook avatar cache
    suspend fun setContactAvatar(contactId: Int, avatarUrl: String?)

    suspend fun findContactById(id: Int): Contact?
}
