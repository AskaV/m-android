package com.spp.android.myapplication.xmlscreens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.databinding.ItemContactRecyclerVievBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar


class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onDeleteClick: (Contact, Int) -> Unit,
    private val onItemClick: (Contact, View) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactRecyclerVievBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick(contacts[pos], binding.avatarImageView)
                }
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

        b.avatarImageView.loadAvatar(contact.avatarUrl)
        b.nameTextView.text = contact.name

        val tn = "avatar_${contact.name}_$position"
        ViewCompat.setTransitionName(b.avatarImageView, tn)

        b.deleteButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDeleteClick(contacts[pos], pos)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun removeContactAt(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreContact(contact: Contact, position: Int) {
        contacts.add(position, contact)
        notifyItemInserted(position)
    }

    fun getContactAt(position: Int): Contact {
        return contacts[position]
    }
}