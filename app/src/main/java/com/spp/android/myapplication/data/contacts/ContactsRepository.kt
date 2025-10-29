package com.spp.android.myapplication.data.contacts

import android.content.Context
import android.provider.ContactsContract
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
@Singleton
class ContactsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val localContacts = MutableStateFlow<List<ContactUi>>(emptyList())

    fun observeContacts(): Flow<List<ContactUi>> = localContacts.asStateFlow()

    fun current(): List<ContactUi> = localContacts.value

    suspend fun addContact(contact: ContactUi) {
        val newList = localContacts.value.toMutableList()
        if (newList.none { it.id == contact.id }) {
            newList += contact
            localContacts.value = newList.sortedBy { it.name.lowercase() }
        }
    }

    suspend fun addContacts(contacts: List<ContactUi>) {
        val existing = localContacts.value.associateBy { it.id }.toMutableMap()
        for (c in contacts) existing.putIfAbsent(c.id, c)
        localContacts.value = existing.values.sortedBy { it.name.lowercase() }
    }

    suspend fun removeContact(id: Int) {
        localContacts.value = localContacts.value.filterNot { it.id == id }
    }

    fun clear() {
        localContacts.value = emptyList()
    }


    suspend fun importFromSystem(): List<ContactUi> {
        val systemContacts = readSystemContacts()
        val merged = (localContacts.value + systemContacts)
            .distinctBy { it.id }
            .sortedBy { it.name.lowercase() }
        localContacts.value = merged
        return merged
    }

    private fun readSystemContacts(): List<ContactUi> {
        val resolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        val seenIds = HashSet<Int>()
        val result = mutableListOf<ContactUi>()

        cursor?.use { c ->
            val idIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (c.moveToNext()) {
                val id = c.getInt(idIdx)
                if (!seenIds.add(id)) continue
                val name = c.getString(nameIdx) ?: "Unnamed"
                val phone = c.getString(numIdx) ?: ""
                result += ContactUi(
                    id = id,
                    name = name,
                    subtitle = phone,
                    avatarUrl = null,
                    transitionName = "contact_$id"
                )
            }
        }
        return result
    }
}