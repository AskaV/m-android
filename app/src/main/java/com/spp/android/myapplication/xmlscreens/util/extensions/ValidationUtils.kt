package com.spp.android.myapplication.xmlscreens.util.extensions

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.spp.android.myapplication.R

object ValidationUtils {

    fun parseNameFromEmail(email: String): String {
        if (email.isBlank()) return "User"
        return email.substringBefore('@')
            .split('.', '_', '-', ' ')
            .filter { it.isNotBlank() }
            .joinToString(" ") { part ->
                part.lowercase().replaceFirstChar { it.titlecase() }
            }
    }

    fun validateEmailAndPassword(
        context: Context,
        emailField: EditText,
        passwordField: EditText,
        emailErrorView: TextView,
        passwordErrorView: TextView
    ): Boolean {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val validPassword =
            password.length >= context.resources.getInteger(R.integer.password_length)

        var allValid = true

        if (!validEmail) {
            emailErrorView.text = context.getString(R.string.email_error_text)
            emailErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            emailErrorView.visibility = View.GONE
        }

        if (!validPassword) {
            passwordErrorView.text = context.getString(R.string.password_error_text)
            passwordErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            passwordErrorView.visibility = View.GONE
        }

        return allValid
    }
}