package com.spp.android.myapplication.presentation.feature.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.ContactPreviewText
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<ContactsContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(ContactsContract.Event.Load)
    }

    fun onEvent(event: ContactsContract.Event) {
        when (event) {
            ContactsContract.Event.Load -> load()
            ContactsContract.Event.BackClicked -> emit(ContactsContract.Effect.NavigateBack)
            ContactsContract.Event.SearchClicked -> emit(ContactsContract.Effect.OpenSearch)
            ContactsContract.Event.AddContactsClicked -> emit(ContactsContract.Effect.OpenAddContacts)
            is ContactsContract.Event.ContactClicked ->
                emit(ContactsContract.Effect.OpenContactProfile(event.item.id))

            is ContactsContract.Event.DeleteClicked -> delete(event.item)
            ContactsContract.Event.ErrorShown ->
                _state.update { it.copy(error = null) }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching {
            // TODO replace with repository call
            demoContacts()
        }.onSuccess { list ->
            _state.update { it.copy(items = list, isLoading = false) }
        }.onFailure { t ->
            _state.update { it.copy(isLoading = false, error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)) }
            emit(ContactsContract.Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED.text(appContext)))
        }
    }

    private fun delete(item: ContactUi) = viewModelScope.launch {
        _state.update { it.copy(items = it.items.filterNot { c -> c.id == item.id }) }
        emit(ContactsContract.Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED.text(appContext)))
    }

    private fun emit(effect: ContactsContract.Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private fun demoContacts(): List<ContactUi> = listOf(
        ContactUi("1", ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1),
        ContactUi("2", ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2),
        ContactUi("3", ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3),
        ContactUi("4", ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4),
        ContactUi("5", ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5),
        ContactUi("6", ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6),
    )
}