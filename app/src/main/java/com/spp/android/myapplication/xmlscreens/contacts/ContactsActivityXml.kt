package com.spp.android.myapplication.xmlscreens.contacts

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R

class ContactsActivityXml : AppCompatActivity() {

    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_contacts_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]

        viewModel.contacts.observe(this) { contactList ->
            val adapter = ContactAdapter(contactList) { contact ->
                Toast.makeText(this, "Удалён: ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = adapter
        }
    }
}