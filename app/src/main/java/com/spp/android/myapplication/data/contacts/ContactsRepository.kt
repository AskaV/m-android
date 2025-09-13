package com.spp.android.myapplication.data.contacts

import android.content.Context
import android.provider.ContactsContract
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun loadContacts(): List<ContactUi> {
        val cr = context.contentResolver
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null, null,
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} COLLATE LOCALIZED ASC"
        )

        val result = mutableListOf<ContactUi>()
        val seen = HashSet<String>()

        cursor?.use { c ->
            val idIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIdx = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val numIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (c.moveToNext()) {
                val id = c.getString(idIdx)
                if (!seen.add(id)) continue
                val name = c.getString(nameIdx) ?: "No name"
                val phone = c.getString(numIdx).orEmpty()
                result.add(ContactUi(id, name, phone))
            }
        }
        return result
    }
}