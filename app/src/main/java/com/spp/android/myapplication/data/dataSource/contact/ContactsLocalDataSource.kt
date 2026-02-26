package com.spp.android.myapplication.data.dataSource.contact

import com.spp.android.myapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsLocalDataSource {

    val localAddedContacts: Flow<List<Contact>>
    suspend fun saveLocalAddedContacts(contacts: List<Contact>)

    val phonebookAvatarCache: Flow<List<Contact>>
    suspend fun savePhonebookAvatarCache(cache: List<Contact>)

    val apiMyContactsCache: Flow<List<Contact>>
    suspend fun saveApiMyContactsCache(contacts: List<Contact>)

    val apiAllUsersCache: Flow<List<Contact>>
    suspend fun saveApiAllUsersCache(users: List<Contact>)

    val pendingMyContactDeletes: Flow<Set<Int>>
    suspend fun savePendingMyContactDeletes(ids: Set<Int>)

    suspend fun addPendingMyDelete(id: Int)
    suspend fun removePendingMyDelete(id: Int)

    val showPhonebook: Flow<Boolean>
    suspend fun setShowPhonebook(value: Boolean)
    suspend fun clearAll()
}