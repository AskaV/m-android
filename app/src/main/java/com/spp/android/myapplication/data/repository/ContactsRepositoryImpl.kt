package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.dataSource.contact.ContactDataSource
import com.spp.android.myapplication.data.storage.ContactsPreferences
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.domain.storage.LocalStorage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource,
    private val contactsPreferences: ContactsPreferences,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        val systemContacts = contactDataSource.fetchContacts()
        val stored = contactsPreferences.contacts.first()

        val overrides = stored.associateBy { it.id }.toMutableMap()

        val migrated = migrateAvatarStyle(overrides)
        if (migrated) {
            contactsPreferences.saveContacts(overrides.values.toList())
        }

        var changed = false

        val result = systemContacts.map { contact ->
            val overrideAvatar = overrides[contact.id]?.avatarUrl
            val systemAvatar = contact.avatarUrl

            val finalAvatar =
                when {
                    !overrideAvatar.isNullOrBlank() -> overrideAvatar
                    !systemAvatar.isNullOrBlank() -> systemAvatar
                    else -> {
                        val generated = generateAvatarUrl(contact.id)
                        overrides[contact.id] = Contact(
                            id = contact.id,
                            name = "",
                            subtitle = "",
                            avatarUrl = generated,
                        )
                        changed = true
                        generated
                    }
                }

            contact.copy(avatarUrl = finalAvatar)
        }

        if (changed) {
            contactsPreferences.saveContacts(overrides.values.toList())
        }

        return result
    }

    override suspend fun setContactAvatar(contactId: Int, avatarUrl: String?) {
        val current = contactsPreferences.contacts.first().toMutableList()

        val idx = current.indexOfFirst { it.id == contactId }
        if (idx >= 0) {
            current[idx] = current[idx].copy(avatarUrl = avatarUrl)
        } else {
            current.add(Contact(id = contactId, name = "", subtitle = "", avatarUrl = avatarUrl))
        }

        contactsPreferences.saveContacts(current)
    }

    private fun generateAvatarUrl(contactId: Int): String {
        return "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_$contactId"
    }

    private fun migrateAvatarStyle(overrides: MutableMap<Int, Contact>): Boolean {
        var changed = false

        overrides.entries.forEach { (id, c) ->
            val url = c.avatarUrl ?: return@forEach
            if (url.contains("/9.x/bottts/")) {
                overrides[id] = c.copy(
                    avatarUrl = "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_$id"
                )
                changed = true
            }
        }

        return changed
    }
}