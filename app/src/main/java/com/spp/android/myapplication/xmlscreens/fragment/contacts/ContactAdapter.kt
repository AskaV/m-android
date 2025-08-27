package com.spp.android.myapplication.xmlscreens.fragment.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.ItemContactRecyclerVievBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.ImageViewExtensions.loadAvatar

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onDeleteClick: (Contact, Int) -> Unit,
    private val onItemClick: (Contact, View) -> Unit,
    private val onItemLongClick: (Int) -> Unit,
    private val onItemSelectToggle: (Int) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()
    private var selectionMode = false

    inner class ContactViewHolder(val binding: ItemContactRecyclerVievBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (selectionMode) {
                        toggleSelection(pos)
                        onItemSelectToggle(pos)
                    } else {
                        onItemClick(contacts[pos], binding.avatarImageView)
                    }
                }
            }

            binding.selectCheck.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    toggleSelection(pos)
                    onItemSelectToggle(pos)
                }
            }

            binding.root.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (!selectionMode) setSelectionMode(true)
                    toggleSelection(pos)
                    onItemLongClick(pos)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactRecyclerVievBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        val b = holder.binding
        val selected = selectedPositions.contains(position)

        b.selectCheck.visibility = if (selectionMode) View.VISIBLE else View.GONE
        b.selectCheck.isChecked = selected

        b.avatarImageView.loadAvatar(contact.avatarUrl)
        b.nameTextView.text = contact.name

        val tn = "avatar_${contact.name}_$position"
        ViewCompat.setTransitionName(b.avatarImageView, tn)

        b.root.setBackgroundResource(
            if (selected) R.drawable.contact_selected_background else R.drawable.contact_outlined
        )

        b.deleteButton.visibility = if (selectionMode) View.INVISIBLE else View.VISIBLE
        b.deleteButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION && !selectionMode) {
                onDeleteClick(contacts[pos], pos)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun setSelectionMode(enabled: Boolean) {
        if (selectionMode == enabled) return
        selectionMode = enabled
        if (!enabled) {
            val prev = selectedPositions.toList()
            selectedPositions.clear()
            prev.forEach { notifyItemChanged(it) }
        }
        notifyDataSetChanged()
    }

    fun isSelectionMode(): Boolean = selectionMode

    fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) selectedPositions.remove(position)
        else selectedPositions.add(position)
        notifyItemChanged(position)
    }

    fun isAnySelected() = selectedPositions.isNotEmpty()

    fun getSelectedPositions(): List<Int> = selectedPositions.sorted()

    fun removeContactAt(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
        val updated = selectedPositions.mapNotNull { old ->
            when {
                old == position -> null
                old > position -> old - 1
                else -> old
            }
        }.toMutableSet()
        selectedPositions.clear(); selectedPositions.addAll(updated)
    }

    fun restoreContact(contact: Contact, position: Int) {
        val safePos = position.coerceIn(0, contacts.size)
        contacts.add(safePos, contact)
        notifyItemInserted(safePos)

        val updated = selectedPositions.map { if (it >= safePos) it + 1 else it }.toMutableSet()
        selectedPositions.clear(); selectedPositions.addAll(updated)
    }

    fun getContactAt(position: Int): Contact = contacts[position]
}