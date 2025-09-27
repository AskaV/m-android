package com.spp.android.myapplication.xmlscreens.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.DialogAddContactBinding
import com.spp.android.myapplication.databinding.MyContactsPageBinding
import com.spp.android.myapplication.xmlscreens.contacts.Contact
import com.spp.android.myapplication.xmlscreens.contacts.ContactAdapter
import com.spp.android.myapplication.xmlscreens.contacts.ContactAdapterListener
import com.spp.android.myapplication.xmlscreens.contacts.ContactsViewModel
import com.spp.android.myapplication.xmlscreens.util.extensions.FakeAddressProvider

class ContactsFragmentXml : Fragment() {

    private var _binding: MyContactsPageBinding? = null
    private val binding get() = _binding ?: error("Binding is null")

    private val viewModel: ContactsViewModel by activityViewModels()

    private val adapter: ContactAdapter by lazy {
        ContactAdapter(mutableListOf(), object : ContactAdapterListener {
            override fun onDeleteContact(contactToDelete: Contact, position: Int) {
                adapter.removeContactAt(position)
                snackbarQueue.addLast(contactToDelete to position)
                if (currentSnackbar?.isShown != true) showNextSnackbar()
            }

            override fun onItemClick(contact: Contact, sharedElementView: View) {
                val transitionName =
                    ViewCompat.getTransitionName(sharedElementView)
                        ?: "avatar_${contact.name}_${System.nanoTime()}"

                val sharedElementExtras =
                    FragmentNavigatorExtras(sharedElementView to transitionName)

                val contactAddress = FakeAddressProvider.forName(contact.name)

                val navAction = ContactsFragmentXmlDirections
                    .actionContactsFragmentXmlToContactDetailFragment(
                        transitionName = transitionName,
                        contactName = contact.name,
                        position = contact.position,
                        avatarUrl = contact.avatarUrl,
                        address = contactAddress
                    )

                findNavController().navigate(navAction, sharedElementExtras)
            }

        })
    }
    private val snackbarQueue = ArrayDeque<Pair<Contact, Int>>()
    private var currentSnackbar: Snackbar? = null
    private var countdownTimer: CountDownTimer? = null
    private val useRealContacts = true

    private val requestContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) loadContactsFromPhone()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyContactsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        enableSwipeToDelete(adapter)

        binding.addContactsText.setOnClickListener { showAddContactDialog() }

        if (useRealContacts) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                loadContactsFromPhone()
            } else {
                requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
            }
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contactList ->
            adapter.replaceAll(contactList.toMutableList())
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
                if (currentSnackbar?.isShown != true) showNextSnackbar()

            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun showNextSnackbar() {
        if (snackbarQueue.isEmpty()) return
        val (contact, position) = snackbarQueue.removeFirst()
        showUndoSnackbar(contact, position)
    }

    private fun showUndoSnackbar(contact: Contact, position: Int, durationSec: Int = 5) {
        var secondsLeft = durationSec
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.deleted_contact_toast_text, secondsLeft),
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
                snackbar.setText(getString(R.string.deleted_contact_toast_text, secondsLeft))
            }

            override fun onFinish() {
                snackbar.dismiss()
            }
        }.start()
    }

    private fun showAddContactDialog() {
        val dialogBinding = DialogAddContactBinding.inflate(layoutInflater)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_contacts_text))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.add_contacts_save_text)) { _, _ ->
                val name = dialogBinding.nameEditText.text.toString()
                val position = dialogBinding.positionEditText.text.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        currentSnackbar?.dismiss()
        _binding = null
    }

}