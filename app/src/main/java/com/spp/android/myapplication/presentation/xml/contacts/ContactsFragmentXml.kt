package com.spp.android.myapplication.presentation.xml.contacts

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.DialogAddContactBinding
import com.spp.android.myapplication.databinding.MyContactsPageBinding
import com.spp.android.myapplication.presentation.xml.tabs.MainTabsFragment
import com.spp.android.myapplication.presentation.util.extensions.FakeAddressProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContactsFragmentXml : Fragment() {

    private var _binding: MyContactsPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    private val snackbarQueue = ArrayDeque<Pair<Contact, Int>>()
    private var currentSnackbar: Snackbar? = null
    private var countdownTimer: CountDownTimer? = null
    private val undoDurationSec = 5

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

        adapter = ContactAdapter(object : ContactAdapterListener {
            override fun onDeleteContact(contact: Contact, position: Int) {
                viewModel.removeAt(position)
            }

            override fun onItemClick(contact: Contact, sharedElementView: View) {
                val transitionName = ViewCompat.getTransitionName(sharedElementView)
                    ?: "avatar_${contact.name}_${System.nanoTime()}"
                val extras = FragmentNavigatorExtras(sharedElementView to transitionName)
                val address = FakeAddressProvider.forName(contact.name)
                val action = ContactsFragmentXmlDirections
                    .actionContactsFragmentXmlToContactDetailFragment(
                        transitionName = transitionName,
                        contactName = contact.name,
                        position = contact.position,
                        avatarUrl = contact.avatarUrl,
                        address = address
                    )
                findNavController().navigate(action, extras)
            }

            override fun onItemLongClick(position: Int) {
                adapter.setSelectionMode(true)
                updateFab()
            }

            override fun onItemSelectToggle(position: Int) {
                if (!adapter.isAnySelected()) adapter.setSelectionMode(false)
                updateFab()
            }
        })
        binding.recyclerView.adapter = adapter
        enableSwipeToDelete(adapter)

        binding.addContactsText.setOnClickListener { showAddContactDialog() }
        binding.backArrow.setOnClickListener {
            (parentFragment as? MainTabsFragment)?.switchToProfile()
        }

        binding.fabDelete.visibility = View.GONE
        binding.fabDelete.setOnClickListener {
            val positions = adapter.getSelectedPositions().sortedDescending()
            viewModel.removeMany(positions)
            adapter.setSelectionMode(false)
            binding.fabDelete.visibility = View.GONE
        }

        if (useRealContacts) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            if (granted) loadContactsFromPhone() else
                requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        }

        viewModel.contacts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { ev ->
                when (ev) {
                    is ContactsViewModel.UiEvent.ShowUndo -> {
                        snackbarQueue.addLast(ev.contact to ev.position)
                        if (currentSnackbar == null || currentSnackbar?.isShown != true) {
                            showNextSnackbar()
                        }
                    }
                }
            }
        }
    }

    private fun updateFab() {
        val visible = adapter.isAnySelected() && adapter.isSelectionMode()
        binding.fabDelete.visibility = if (visible) View.VISIBLE else View.GONE
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
        val helper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val pos = vh.bindingAdapterPosition
                if (pos !in 0 until adapter.itemCount) return

                if (adapter.isSelectionMode()) {
                    binding.recyclerView.adapter?.notifyItemChanged(pos)
                    return
                }

                viewModel.removeAt(pos)
            }
        })
        helper.attachToRecyclerView(binding.recyclerView)
    }

    private fun showNextSnackbar() {
        if (!isAdded || snackbarQueue.isEmpty()) return
        val (contact, position) = snackbarQueue.removeFirst()
        showUndoSnackbar(contact, position, undoDurationSec)
    }

    private fun showUndoSnackbar(contact: Contact, position: Int, durationSec: Int) {
        val anchor = binding.root

        var secondsLeft = durationSec
        val text = { getString(R.string.deleted_contact_toast_text, secondsLeft) }

        val snackbar = Snackbar.make(anchor, text(), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.return_contact_toast_text) {
            viewModel.restore(contact, position)
            countdownTimer?.cancel()
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                currentSnackbar = null
                if (isAdded && view != null) showNextSnackbar()
            }
        })
        snackbar.show()
        currentSnackbar = snackbar

        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer((durationSec * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft--
                if (currentSnackbar === snackbar) {
                    snackbar.setText(text())
                }
            }

            override fun onFinish() {
                if (currentSnackbar === snackbar) snackbar.dismiss()
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        currentSnackbar?.dismiss()
        currentSnackbar = null
        snackbarQueue.clear()
        _binding = null
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
                    viewModel.add(newContact, index = 0)
                }
            }
            .setNegativeButton(getString(R.string.add_contacts_cancel_text), null)
            .show()
    }
}