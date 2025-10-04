package com.spp.android.myapplication.xmlscreens.fragment.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter

import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.ItemContactRecyclerVievBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar

class ContactAdapter(
    private val onDeleteClick: (Contact, Int) -> Unit,
    private val onItemClick: (Contact, View) -> Unit,
    private val onItemLongClick: (Int) -> Unit,
    private val onItemSelectToggle: (Int) -> Unit
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DIFF) {

    private val selectedKeys = mutableSetOf<String>()
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
                        onItemClick(getItem(pos), binding.avatarImageView)
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
        val contact = getItem(position)
        val b = holder.binding

        val key = stableKey(contact)
        val selected = selectedKeys.contains(key)

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
                onDeleteClick(getItem(pos), pos)
            }
        }
    }

    override fun getItemId(position: Int): Long =
        stableKey(getItem(position)).hashCode().toLong()

    fun setSelectionMode(enabled: Boolean) {
        if (selectionMode == enabled) return
        selectionMode = enabled
        if (!enabled) {
            selectedKeys.clear()
        }
        notifyDataSetChanged()
    }

    fun isSelectionMode(): Boolean = selectionMode

    fun toggleSelection(position: Int) {
        val key = stableKey(getItem(position))
        if (selectedKeys.contains(key)) selectedKeys.remove(key) else selectedKeys.add(key)
        notifyItemChanged(position)
    }

    fun isAnySelected() = selectedKeys.isNotEmpty()

    fun getSelectedPositions(): List<Int> =
        currentList.mapIndexedNotNull { index, c -> if (selectedKeys.contains(stableKey(c))) index else null }

    fun removeAt(position: Int) {
        if (position !in 0 until itemCount) return
        val mutable = currentList.toMutableList()
        val removed = mutable.removeAt(position)
        selectedKeys.remove(stableKey(removed))
        submitList(mutable)
    }

    fun restoreContact(contact: Contact, position: Int) {
        val mutable = currentList.toMutableList()
        val safePos = position.coerceIn(0, mutable.size)
        mutable.add(safePos, contact)
        submitList(mutable)
    }

    fun getContactAt(position: Int): Contact = getItem(position)

    private fun stableKey(c: Contact): String =
        "${c.name}|${c.position}|${c.avatarUrl}"

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                (oldItem.name == newItem.name && oldItem.avatarUrl == newItem.avatarUrl)
            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem == newItem
        }
    }
}