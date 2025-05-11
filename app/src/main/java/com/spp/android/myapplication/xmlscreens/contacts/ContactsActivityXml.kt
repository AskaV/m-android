package com.spp.android.myapplication.xmlscreens.contacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R

class ContactsActivityXml : AppCompatActivity() {

    private lateinit var viewModel: ContactsViewModel
    private val useRealContacts = true // true - download contacts from the phone book. false - download fake contacts.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_contacts_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]

        if (useRealContacts) {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                loadContactsFromPhone()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 100)
            }
        }

        viewModel.contacts.observe(this) { contactList ->
            val adapter = ContactAdapter(contactList) { contact ->
                Toast.makeText(this, "Deleted: ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = adapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            loadContactsFromPhone()
        } else {
            Toast.makeText(this, "Access to contacts is denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContactsFromPhone() {
        val contactList = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: continue
                val number = it.getString(numberIndex) ?: ""
                val avatar = "https://i.pravatar.cc/150?u=$name"
                contactList.add(Contact(name, number, avatar))
            }
        }

        viewModel.setContacts(contactList)
    }
}