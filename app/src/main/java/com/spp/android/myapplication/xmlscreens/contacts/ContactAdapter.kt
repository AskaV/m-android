package com.spp.android.myapplication.xmlscreens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar


class ContactAdapter(
    private val contacts: List<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val professionTextView: TextView = itemView.findViewById(R.id.positionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_recycler_viev, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.avatarImageView.loadAvatar(contact.avatarUrl)
        holder.nameTextView.text = contact.name
    }
    override fun getItemCount(): Int = contacts.size
}