package com.spp.android.myapplication.presentation.xml.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.ItemContactRecyclerVievBinding
import com.spp.android.myapplication.presentation.util.extensions.loadAvatar

class ContactAdapter(
    private val onDeleteClick: (Contact, Int) -> Unit,
    private val onItemClick: (Contact, View) -> Unit,
    private val onItemLongClick: (Int) -> Unit,
    private val onItemSelectToggle: (Int) -> Unit
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DIFF) {

    private val selectedKeys = mutableSetOf<String>()
    private var selectionMode = false

    init {
        setHasStableIds(true)
    }

    inner class ContactViewHolder(val binding: ItemContactRecyclerVievBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (selectionMode) {
                        toggleSelection(pos); onItemSelectToggle(pos)
                    } else {
                        onItemClick(getItem(pos), binding.avatarImageView)
                    }
                }
            }
            binding.selectCheck.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    toggleSelection(pos); onItemSelectToggle(pos)
                }
            }
            binding.root.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (!selectionMode) setSelectionMode(true)
                    toggleSelection(pos); onItemLongClick(pos)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactRecyclerVievBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
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
        if (!enabled) selectedKeys.clear()
        notifyDataSetChanged()
    }

    fun isSelectionMode(): Boolean = selectionMode
    fun isAnySelected() = selectedKeys.isNotEmpty()

    fun toggleSelection(position: Int) {
        val key = stableKey(getItem(position))
        if (!selectedKeys.add(key)) selectedKeys.remove(key)
        notifyItemChanged(position)
    }

    fun getSelectedPositions(): List<Int> =
        currentList.mapIndexedNotNull { index, c ->
            if (selectedKeys.contains(stableKey(c))) index else null
        }

    fun itemAt(position: Int): Contact? = currentList.getOrNull(position)

    private fun stableKey(c: Contact): String =
        "${c.name}|${c.position}|${c.avatarUrl}"

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem.name == newItem.name &&
                        oldItem.avatarUrl == newItem.avatarUrl &&
                        oldItem.position == newItem.position

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem == newItem
        }
    }
}