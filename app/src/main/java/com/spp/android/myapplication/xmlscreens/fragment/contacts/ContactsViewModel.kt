package com.spp.android.myapplication.xmlscreens.fragment.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>(emptyList())
    val contacts: LiveData<List<Contact>> = _contacts

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun setContacts(list: List<Contact>) {
        _contacts.value = list
    }

    fun add(contact: Contact, index: Int = 0) {
        val cur = _contacts.value.orEmpty().toMutableList()
        cur.add(index.coerceIn(0, cur.size), contact)
        _contacts.value = cur
    }

    fun removeAt(position: Int) {
        val cur = _contacts.value.orEmpty().toMutableList()
        if (position !in cur.indices) return
        val removed = cur.removeAt(position)
        _contacts.value = cur
        viewModelScope.launch {
            _events.send(UiEvent.ShowUndo(removed, position))
        }
    }

    fun removeMany(positionsDesc: List<Int>) {
        if (positionsDesc.isEmpty()) return
        val cur = _contacts.value.orEmpty().toMutableList()
        val removedPairs = mutableListOf<Pair<Contact, Int>>()

        positionsDesc.forEach { pos ->
            if (pos in cur.indices) {
                val removed = cur.removeAt(pos)
                removedPairs += removed to pos
            }
        }
        _contacts.value = cur

        viewModelScope.launch {
            for ((c, p) in removedPairs) {
                _events.send(UiEvent.ShowUndo(c, p))
            }
        }
    }

    fun restore(contact: Contact, position: Int) {
        val cur = _contacts.value.orEmpty().toMutableList()
        cur.add(position.coerceIn(0, cur.size), contact)
        _contacts.value = cur
    }

    sealed interface UiEvent {
        data class ShowUndo(val contact: Contact, val position: Int) : UiEvent
    }
}