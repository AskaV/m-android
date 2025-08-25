package com.spp.android.myapplication.xmlscreens.util.extensions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginViewModel(
    private val savedState: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_EMAIL = "email_text"
        private const val KEY_PASSWORD = "password_text"
    }

    val email = savedState.getStateFlow(KEY_EMAIL, "")
    val password = savedState.getStateFlow(KEY_PASSWORD, "")

    fun setEmail(value: String) {
        savedState[KEY_EMAIL] = value
    }

    fun setPassword(value: String) {
        savedState[KEY_PASSWORD] = value
    }
}