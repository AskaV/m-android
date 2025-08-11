package com.spp.android.myapplication.xmlscreens.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.contacts.Contact
import com.spp.android.myapplication.xmlscreens.contacts.ContactAdapter
import com.spp.android.myapplication.xmlscreens.contacts.ContactsViewModel

class ContactsFragmentXml : Fragment(R.layout.my_contacts_page) {

    private lateinit var viewModel: ContactsViewModel
    private lateinit var adapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView

    private val snackbarQueue = ArrayDeque<Pair<Contact, Int>>()
    private var currentSnackbar: Snackbar? = null
    private var countdownTimer: CountDownTimer? = null
    private val useRealContacts = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity())[ContactsViewModel::class.java]

        view.findViewById<TextView>(R.id.addContactsText).setOnClickListener {
            showAddContactDialog()
        }

        if (useRealContacts) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                loadContactsFromPhone()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 100)
            }
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contactList ->
            val mutableList = contactList.toMutableList()
            adapter = ContactAdapter(
                mutableList,
                onDeleteClick = { contactToDelete, position ->
                    adapter.removeContactAt(position)
                    snackbarQueue.addLast(Pair(contactToDelete, position))
                    if (currentSnackbar == null || !currentSnackbar!!.isShown) {
                        showNextSnackbar()
                    }
                },
                onItemClick = { contact, sharedView ->
                    findNavController().navigate(
                        ContactsFragmentXmlDirections.actionContactsFragmentXmlToContactDetailFragment(
                            contact.name,
                            contact.position,
                            contact.avatarUrl
                        )
                    )
                }
            )
            recyclerView.adapter = adapter
            enableSwipeToDelete(adapter)
        }
    }

    private fun loadContactsFromPhone() {
        val contactList = mutableListOf<Contact>()
        val cursor = requireContext().contentResolver.query(
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

    private fun enableSwipeToDelete(adapter: ContactAdapter) {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val contactToDelete = adapter.getContactAt(position)
                adapter.removeContactAt(position)
                snackbarQueue.addLast(Pair(contactToDelete, position))
                if (currentSnackbar == null || !currentSnackbar!!.isShown) {
                    showNextSnackbar()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showNextSnackbar() {
        if (snackbarQueue.isEmpty()) return
        val (contact, position) = snackbarQueue.removeFirst()
        showUndoSnackbar(contact, position)
    }

    private fun showUndoSnackbar(contact: Contact, position: Int, durationSec: Int = 5) {
        var secondsLeft = durationSec
        val snackbar = Snackbar.make(
            requireView(),
            getString(R.string.deleted_contact_toast_text) + " ($secondsLeft)",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(R.string.return_contact_toast_text) {
            adapter.restoreContact(contact, position)
            countdownTimer?.cancel()
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                currentSnackbar = null
                showNextSnackbar()
            }
        })
        snackbar.show()
        currentSnackbar = snackbar
        countdownTimer = object : CountDownTimer((durationSec * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft--
                snackbar.setText(getString(R.string.deleted_contact_toast_text) + " ($secondsLeft)")
            }

            override fun onFinish() {
                snackbar.dismiss()
            }
        }.start()
    }

    private fun showAddContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
        val positionEditText = dialogView.findViewById<EditText>(R.id.positionEditText)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_contacts_text))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.add_contacts_save_text)) { _, _ ->
                val name = nameEditText.text.toString()
                val position = positionEditText.text.toString()
                if (name.isNotBlank() && position.isNotBlank()) {
                    val newContact = Contact(
                        name = name,
                        position = position,
                        avatarUrl = "https://i.pravatar.cc/150?img=${(1..70).random()}"
                    )

                    val currentList = viewModel.contacts.value?.toMutableList() ?: mutableListOf()
                    currentList.add(0, newContact)
                    viewModel.setContacts(currentList)
                    adapter.notifyItemInserted(0)
                }
            }
            .setNegativeButton(getString(R.string.add_contacts_cancel_text), null)
            .show()
    }
}