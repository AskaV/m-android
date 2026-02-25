package com.spp.android.myapplication.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spp.android.myapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class ContactsPreferences
@Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val localAddedJson = stringPreferencesKey("local_added_contacts_json")
    private val phonebookAvatarJson = stringPreferencesKey("phonebook_avatar_cache_json")

    val localAddedContacts: Flow<List<Contact>> =
        dataStore.data.map { prefs -> decode(prefs[localAddedJson].orEmpty()) }

    val phonebookAvatarCache: Flow<List<Contact>> =
        dataStore.data.map { prefs -> decode(prefs[phonebookAvatarJson].orEmpty()) }

    suspend fun saveLocalAddedContacts(contacts: List<Contact>) {
        dataStore.edit { prefs -> prefs[localAddedJson] = encode(contacts) }
    }

    suspend fun savePhonebookAvatarCache(cache: List<Contact>) {
        dataStore.edit { prefs -> prefs[phonebookAvatarJson] = encode(cache) }
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
