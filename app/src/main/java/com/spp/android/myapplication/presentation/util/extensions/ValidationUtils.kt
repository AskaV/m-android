package com.spp.android.myapplication.presentation.util.extensions

import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.spp.android.myapplication.R

object ValidationUtils {
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MIN_NAME_LETTERS = 3
    private const val MIN_PHONE_DIGITS = 10
    private const val MAX_PHONE_DIGITS = 15

    fun validateEmailAndPassword(
        emailField: EditText,
        passwordField: EditText,
        emailErrorView: TextView,
        passwordErrorView: TextView,
    ): Boolean {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val validPassword = password.length >= MIN_PASSWORD_LENGTH

        var allValid = true

        if (!validEmail) {
            emailErrorView.setText(R.string.email_error_text)
            emailErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            emailErrorView.visibility = View.GONE
        }

        if (!validPassword) {
            passwordErrorView.setText(R.string.password_error_text)
            passwordErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            passwordErrorView.visibility = View.GONE
        }

        return allValid
    }

    fun validateNameAndPhone(
        nameField: EditText,
        phoneField: EditText,
        nameErrorView: TextView,
        phoneErrorView: TextView,
    ): Boolean {
        val name =
            nameField.text
                ?.toString()
                ?.trim()
                .orEmpty()
        val phoneRaw =
            phoneField.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val lettersCount = name.count { it.isLetter() }
        val isNameValid = lettersCount >= MIN_NAME_LETTERS

        val digits = normalizePhone(phoneRaw)
        val isPhoneValid = digits.length in MIN_PHONE_DIGITS..MAX_PHONE_DIGITS

        var allValid = true

        if (!isNameValid) {
            nameErrorView.setText(R.string.signup_user_name_error_min3)
            nameErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            nameErrorView.visibility = View.GONE
        }

        if (!isPhoneValid) {
            phoneErrorView.setText(R.string.signup_phone_error_min10)
            phoneErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            phoneErrorView.visibility = View.GONE
        }

        return allValid
    }

    fun normalizePhone(phone: String): String = phone.filter { it.isDigit() }

    fun parseNameFromEmail(email: String): String =
        email
            .substringBefore("@")
            .split(".", "_", "-")
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}
