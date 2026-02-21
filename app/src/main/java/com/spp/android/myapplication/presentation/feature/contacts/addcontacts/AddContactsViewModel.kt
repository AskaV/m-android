package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.remote.api.UsersApi
import com.spp.android.myapplication.data.remote.dto.toContact
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.AddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.Load
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.MassAddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.SearchClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.ToggleSelect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val usersApi: UsersApi,
    private val contactsRepo: ContactsRepository,
    private val authPreferences: AuthPreferences,
) : ViewModel() {
    private val _state = MutableStateFlow(AddContactsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Load)
    }

    fun onEvent(event: AddContactsContract.Event) {
        when (event) {
            is Load -> load()
            is BackClicked -> sendEffect(Effect.NavigateBack)

            is SearchClicked -> sendEffect(Effect.OpenSearch)

            is ToggleSelect -> {
                _state.update { st ->
                    val ns = st.selected.toMutableSet().apply {
                        if (contains(event.toggleSelect.id)) {
                            remove(event.toggleSelect.id)
                        } else {
                            add(
                                event.toggleSelect.id,
                            )
                        }
                    }
                    st.copy(selected = ns)
                }
            }

            is MassAddClicked -> viewModelScope.launch {
                val ids = _state.value.selected.toList()
                if (ids.isEmpty()) return@launch

                runCatching {
                    ids.forEach { id ->
                        val token = authPreferences.accessToken.first()
                        val userId = authPreferences.getUserId()
                        contactsRepo.addUserContactRemote(
                            userId = userId, accessToken = token, contactId = id
                        ).getOrThrow()
                    }
                }.onSuccess {
                    _state.update { st ->
                        st.copy(
                            items = st.items.filterNot { st.selected.contains(it.id) },
                            selected = emptySet(),
                        )
                    }
                    sendEffect(Effect.ShowMessage("Added ${ids.size} contact(s)"))
                    sendEffect(Effect.NavigateBack)
                }.onFailure {
                    sendEffect(Effect.ShowMessage("Failed to add contacts"))
                }
            }

            is ErrorShown -> _state.update { it.copy() }

            is AddClicked -> viewModelScope.launch {
                runCatching {
                    val token = authPreferences.accessToken.first()
                    val userId = authPreferences.getUserId()

                    contactsRepo.addUserContactRemote(
                        userId = userId, accessToken = token, contactId = event.dddClicked.id
                    ).getOrThrow()
                }.onSuccess {
                    _state.update { st -> st.copy(items = st.items.filterNot { it.id == event.dddClicked.id }) }
                    sendEffect(Effect.ShowMessage("Added ${event.dddClicked.name}"))
                }.onFailure {
                    sendEffect(Effect.ShowMessage("Failed to add ${event.dddClicked.name}"))
                }
            }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = "") }

        runCatching {
            val token = authPreferences.accessToken.first()
            val bearer = "Bearer $token"

            val response = usersApi.getAllUsers(bearer)

            if (!response.isSuccessful) {
                error("HTTP ${response.code()}")
            }

            val body = response.body() ?: error("Empty body")

            if (body.status != "success") {
                error(body.message ?: "Server error")
            }

            val users = body.data?.users ?: emptyList()

            users.map { it.toContact() }

        }.onSuccess { contacts ->
            _state.update { it.copy(items = contacts, isLoading = false) }
        }.onFailure { e ->
            _state.update { it.copy(isLoading = false, error = e.message.orEmpty()) }
            sendEffect(Effect.ShowMessage("Не удалось загрузить пользователей"))
        }
    }

    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}
