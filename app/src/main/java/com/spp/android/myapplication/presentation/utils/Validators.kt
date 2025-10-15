package com.spp.android.myapplication.presentation.utils

import android.util.Patterns

import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.S
import com.spp.android.myapplication.presentation.texts.TextKey

object Validate {

    fun email(email: String): TextKey? = when {
        email.isBlank() -> AppText.Login.BLANK_EMAIL_ERROR
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            AppText.Login.EMAIL_ERROR
        else -> null
    }

    fun password(password: String): S? =
        if (password.length < AppText.Integers.PASSWORD_MIN_LENGTH)
            AppText.Error.PASSWORD_ERROR else null

    fun username(username: String): S? =
        if (username.count { it.isLetter() } < AppText.Integers.USERNAME_MIN_LENGTH)
            AppText.Error.USERNAME_ERROR else null

    fun phone(phone: String): S? =
        if (!phone.matches(Regex("^\\+?[0-9]{10,15}$")))
            AppText.Error.PHONE_ERROR else null
}