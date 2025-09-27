package com.spp.android.myapplication.xmlscreens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.databinding.ItemContactRecyclerVievBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.ImageViewExtensions.loadAvatar

interface ContactAdapterListener {
    fun onDeleteContact(contact: Contact, position: Int)
    fun onItemClick(contact: Contact, view: View)
}

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val listener: ContactAdapterListener

) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactRecyclerVievBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(contacts[pos], binding.avatarImageView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactRecyclerVievBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            avatarImageView.loadAvatar(contact.avatarUrl)
            nameTextView.text = contact.name
            deleteButton.setOnClickListener {
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteContact(contacts[currentPosition], currentPosition)
                }
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
    fun replaceAll(newItems: List<Contact>) {
        contacts.clear()
        contacts.addAll(newItems)
        notifyDataSetChanged()
    }
}