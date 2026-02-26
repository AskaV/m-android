package com.spp.android.myapplication.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spp.android.myapplication.data.dataSource.contact.ContactsLocalDataSource
import com.spp.android.myapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class ContactsPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ContactsLocalDataSource {

    private val localAddedJson = stringPreferencesKey("local_added_contacts_json")
    private val phonebookAvatarJson = stringPreferencesKey("phonebook_avatar_cache_json")
    private val apiMyContactsJson = stringPreferencesKey("api_my_contacts_json")
    private val apiAllUsersJson = stringPreferencesKey("api_all_users_json")
    private val apiMyPendingDeletesJson =
        stringPreferencesKey("api_my_contacts_pending_deletes_json")
    private val showPhonebookKey = booleanPreferencesKey("show_phonebook")


    override val localAddedContacts: Flow<List<Contact>> =
        dataStore.data.map { decode(it[localAddedJson].orEmpty()) }

    override val phonebookAvatarCache: Flow<List<Contact>> =
        dataStore.data.map { decode(it[phonebookAvatarJson].orEmpty()) }

    override val apiMyContactsCache: Flow<List<Contact>> =
        dataStore.data.map { decode(it[apiMyContactsJson].orEmpty()) }

    override val apiAllUsersCache: Flow<List<Contact>> =
        dataStore.data.map { decode(it[apiAllUsersJson].orEmpty()) }

    override val showPhonebook: Flow<Boolean> = dataStore.data.map { it[showPhonebookKey] ?: false }


    override suspend fun saveLocalAddedContacts(contacts: List<Contact>) {
        dataStore.edit { it[localAddedJson] = encode(contacts) }
    }

    override suspend fun savePhonebookAvatarCache(cache: List<Contact>) {
        dataStore.edit { it[phonebookAvatarJson] = encode(cache) }
    }

    override suspend fun saveApiMyContactsCache(contacts: List<Contact>) {
        dataStore.edit { it[apiMyContactsJson] = encode(contacts) }
    }

    override suspend fun saveApiAllUsersCache(users: List<Contact>) {
        dataStore.edit { it[apiAllUsersJson] = encode(users) }
    }

    override val pendingMyContactDeletes: Flow<Set<Int>> = dataStore.data.map { prefs ->
        decodeIds(prefs[apiMyPendingDeletesJson].orEmpty())
    }

    override suspend fun savePendingMyContactDeletes(ids: Set<Int>) {
        dataStore.edit { it[apiMyPendingDeletesJson] = encodeIds(ids) }
    }

    override suspend fun setShowPhonebook(value: Boolean) {
        dataStore.edit { it[showPhonebookKey] = value }
    }

    override suspend fun addPendingMyDelete(id: Int) {
        dataStore.edit { prefs ->
            val cur = decodeIds(prefs[apiMyPendingDeletesJson].orEmpty()).toMutableSet()
            cur.add(id)
            prefs[apiMyPendingDeletesJson] = encodeIds(cur)
        }
    }

    override suspend fun removePendingMyDelete(id: Int) {
        dataStore.edit { prefs ->
            val cur = decodeIds(prefs[apiMyPendingDeletesJson].orEmpty()).toMutableSet()
            cur.remove(id)
            prefs[apiMyPendingDeletesJson] = encodeIds(cur)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    private fun encodeIds(ids: Set<Int>): String =
        JSONArray().apply { ids.forEach { put(it) } }.toString()

    private fun decodeIds(json: String): Set<Int> {
        if (json.isBlank()) return emptySet()
        val arr = JSONArray(json)
        val out = LinkedHashSet<Int>()
        for (i in 0 until arr.length()) out.add(arr.getInt(i))
        return out
    }

    private fun encode(list: List<Contact>): String {
        val array = JSONArray()
        list.forEach { contact ->
            val obj = JSONObject()
            obj.put("id", contact.id)
            obj.put("name", contact.name)
            obj.put("subtitle", contact.subtitle)
            obj.put("avatarUrl", contact.avatarUrl ?: "")
            obj.put("transitionName", contact.transitionName ?: "")
            array.put(obj)
        }
        return array.toString()
    }

    private fun decode(json: String): List<Contact> {
        if (json.isBlank()) return emptyList()

        val array = JSONArray(json)
        val result = mutableListOf<Contact>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                Contact(
                    id = obj.getInt("id"),
                    name = obj.optString("name"),
                    subtitle = obj.optString("subtitle"),
                    avatarUrl = obj.optString("avatarUrl").takeIf { it.isNotBlank() },
                    transitionName = obj.optString("transitionName").takeIf { it.isNotBlank() },
                ),
            )
        }
        return result
    }
}