package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactsRepo: ContactsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddContactsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var baseFiltered: List<Contact> = emptyList()

    init {
        observeAllUsersCache()
        onEvent(Event.Load)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.Load -> refresh()

            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.SearchClicked -> _state.update { it.copy(isSearchOpen = true) }
            is Event.SearchClosed -> _state.update {
                it.copy(
                    isSearchOpen = false,
                    query = "",
                    items = baseFiltered,
                    selected = emptySet(),
                    isSelectionMode = false,
                )
            }

            is Event.QueryChanged -> _state.update { st ->
                val q = event.query
                val visible = applyQuery(baseFiltered, q)
                val ids = visible.map { it.id }.toSet()

                st.copy(
                    query = q,
                    items = visible,
                    selected = st.selected.intersect(ids),
                    isSelectionMode = st.isSelectionMode && st.selected.intersect(ids).isNotEmpty(),
                )
            }

            is Event.MassAddClicked -> massAddSelected()

            is Event.AddClicked -> addOne(event.dddClicked.id, event.dddClicked.name)

            is Event.ErrorShown -> _state.update { it.copy(error = "") }

            is Event.UserLongClicked -> toggleSelectionMode(event.contact.id)

            is Event.UserClicked -> {
                if (_state.value.isSelectionMode) {
                    toggleSelection(event.contact.id)
                }
            }

            is Event.ExitSelectionMode -> _state.update {
                it.copy(selected = emptySet(), isSelectionMode = false)
            }
        }
    }

    private fun observeAllUsersCache() = viewModelScope.launch {
        combine(
            contactsRepo.apiAllUsers,
            contactsRepo.apiMyContacts,
            contactsRepo.localAdded,
        ) { allUsers, myContacts, localAdded ->
            val bannedIds = (myContacts + localAdded).map { it.id }.toSet()
            allUsers.filterNot { it.id in bannedIds }
        }.collect { filtered ->
            baseFiltered = filtered

            _state.update { st ->
                val visible = applyQuery(baseFiltered, st.query)
                val existingIds = visible.map { it.id }.toSet()

                st.copy(
                    items = visible,
                    selected = st.selected.intersect(existingIds),
                    isSelectionMode = st.isSelectionMode && st.selected.intersect(existingIds)
                        .isNotEmpty(),
                )
            }
        }
    }

    private fun refresh() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = "") }

        contactsRepo.refreshAllUsers().onSuccess {
            _state.update { it.copy(isLoading = false) }
        }.onFailure { e ->
            _state.update { it.copy(isLoading = false, error = e.message.orEmpty()) }
            sendEffect(Effect.ShowMessage("Не удалось обновить список пользователей"))
        }
    }


    private fun toggleSelectionMode(id: Int) {
        _state.update { st ->
            if (st.isSelectionMode) {
                val newSelected = st.selected.toMutableSet().apply {
                    if (contains(id)) remove(id) else add(id)
                }
                st.copy(selected = newSelected, isSelectionMode = newSelected.isNotEmpty())
            } else {
                st.copy(selected = setOf(id), isSelectionMode = true)
            }
        }
    }

    private fun applyQuery(list: List<Contact>, query: String): List<Contact> {
        val q = query.trim()
        if (q.isEmpty()) return list
        val lower = q.lowercase()
        return list.filter { c ->
            c.name.lowercase().contains(lower) || c.subtitle.lowercase().contains(lower)
        }
    }

    private fun toggleSelection(id: Int) {
        _state.update { st ->
            val newSelected = st.selected.toMutableSet().apply {
                if (contains(id)) remove(id) else add(id)
            }
            st.copy(selected = newSelected, isSelectionMode = newSelected.isNotEmpty())
        }
    }

    private fun addOne(id: Int, name: String) = viewModelScope.launch {
        val contact = _state.value.items.firstOrNull { it.id == id } ?: return@launch

        contactsRepo.addContactOfflineFirst(contact).onSuccess {
            _state.update { st -> st.copy(items = st.items.filterNot { it.id == id }) }
            sendEffect(Effect.ShowMessage("Added $name"))
        }.onFailure {
            sendEffect(Effect.ShowMessage("Failed to add $name"))
        }
    }

    private fun massAddSelected() = viewModelScope.launch {
        val ids = _state.value.selected.toList()
        if (ids.isEmpty()) return@launch

        val itemsById = _state.value.items.associateBy { it.id }
        val toAdd = ids.mapNotNull { itemsById[it] }

        val okIds = mutableSetOf<Int>()
        var failCount = 0

        toAdd.forEach { c ->
            contactsRepo.addContactOfflineFirst(c).onSuccess { okIds.add(c.id) }
                .onFailure { failCount++ }
        }

        _state.update { st ->
            st.copy(
                items = st.items.filterNot { it.id in okIds },
                selected = emptySet(),
            )
        }

        when {
            failCount == 0 -> {
                sendEffect(Effect.ShowMessage("Added ${okIds.size} contact(s)"))
                sendEffect(Effect.NavigateBack)
            }

            okIds.isEmpty() -> sendEffect(Effect.ShowMessage("Failed to add contacts"))
            else -> sendEffect(Effect.ShowMessage("Added ${okIds.size}, failed $failCount"))
        }
    }

    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}