package com.spp.android.myapplication.xmlscreens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spp.android.myapplication.R

class ContactAdapter(
    private val contacts: List<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.nameTextView)
        val position = view.findViewById<TextView>(R.id.positionTextView)
        val avatar = view.findViewById<ImageView>(R.id.avatarImageView)
        val delete = view.findViewById<ImageButton>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_recycler_viev, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name
        holder.position.text = contact.position
        holder.avatar.setImageResource(contact.avatarResId)
        holder.delete.setOnClickListener { onDeleteClick(contact) }
    }

    override fun getItemCount(): Int = contacts.size
}