package com.spp.android.myapplication.presentation.xml.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val savedState: SavedStateHandle,
) : ViewModel() {
    data class UiState(
        val email: String = "",
        val password: String = "",
    )

    private val keyEmail = "login_email"
    private val keyPassword = "login_password"

    private val _state =
        MutableStateFlow(
            UiState(
                email = savedState.get<String>(keyEmail) ?: "",
                password = savedState.get<String>(keyPassword) ?: "",
            ),
        )
    val state: StateFlow<UiState> = _state

    fun setEmail(value: String) {
        if (value == _state.value.email) return
        _state.value = _state.value.copy(email = value)
        savedState[keyEmail] = value
    }

    fun setPassword(value: String) {
        if (value == _state.value.password) return
        _state.value = _state.value.copy(password = value)
        savedState[keyPassword] = value
    }
}
