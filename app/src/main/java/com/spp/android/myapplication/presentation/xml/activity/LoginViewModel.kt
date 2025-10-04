package com.spp.android.myapplication.presentation.xml.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val savedState: SavedStateHandle
) : ViewModel() {

    data class UiState(
        val email: String = "",
        val password: String = ""
    )

    private val KEY_EMAIL = "login_email"
    private val KEY_PASS  = "login_password"

    private val _state = MutableStateFlow(
        UiState(
            email = savedState.get<String>(KEY_EMAIL) ?: "",
            password = savedState.get<String>(KEY_PASS) ?: ""
        )
    )
    val state: StateFlow<UiState> = _state

    fun setEmail(value: String) {
        if (value == _state.value.email) return
        _state.value = _state.value.copy(email = value)
        savedState[KEY_EMAIL] = value
    }

    fun setPassword(value: String) {
        if (value == _state.value.password) return
        _state.value = _state.value.copy(password = value)
        savedState[KEY_PASS] = value
    }
}