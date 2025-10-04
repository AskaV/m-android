package com.spp.android.myapplication.xmlscreens.util.extensions

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.spp.android.myapplication.R

object ValidationUtils {

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

    fun validateNameAndPhone(
        context: Context,
        nameField: EditText,
        phoneField: EditText,
        nameErrorView: TextView,
        phoneErrorView: TextView
    ): Boolean {
        val name = nameField.text?.toString()?.trim().orEmpty()
        val phoneRaw = phoneField.text?.toString()?.trim().orEmpty()
        context.resources.getInteger(R.integer.password_length)
        val minNameLetters = context.resources.getInteger(R.integer.min_name_length)
        val minPhoneDigits =  context.resources.getInteger(R.integer.min_phone_digits)

        val lettersCount = name.count { it.isLetter() }
        val isNameValid = lettersCount >= minNameLetters

        val digits = normalizePhone(phoneRaw)
        val isPhoneValid = digits.length in minPhoneDigits..15

        var allValid = true

        if (!isNameValid) {
            nameErrorView.text = context.getString(R.string.signup_user_name_error_min3)
            nameErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            nameErrorView.visibility = View.GONE
        }

        if (!isPhoneValid) {
            phoneErrorView.text = context.getString(R.string.signup_phone_error_min10)
            phoneErrorView.visibility = View.VISIBLE
            allValid = false
        } else {
            phoneErrorView.visibility = View.GONE
        }

        return allValid
    }

    fun normalizePhone(phone: String): String = phone.filter { it.isDigit() }

    fun parseNameFromEmail(email: String): String {
        return email.substringBefore("@")
            .split(".", "_", "-")
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
    }
}