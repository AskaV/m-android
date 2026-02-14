package com.spp.android.myapplication.xmlscreens.fragment.contacts

import android.view.View

interface ContactAdapterListener {
    fun onDeleteContact(contact: Contact, position: Int)
    fun onItemClick(contact: Contact, view: View)
    fun onItemLongClick(position: Int)
    fun onItemSelectToggle(position: Int)
}