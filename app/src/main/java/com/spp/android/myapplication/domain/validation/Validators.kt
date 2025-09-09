package com.spp.android.myapplication.domain.validation

import android.content.Context
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.text


fun validateEmail(ctx: Context, email: String): String? {
    return if (email.isBlank()) {
        AppText.Login.BLANK_EMAIL_ERROR.text(ctx)
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        AppText.Login.EMAIL_ERROR.text(ctx)
    } else null
}

fun validatePassword(ctx: Context, password: String): String? {
    return if (password.length < AppText.Integers.PASSWORD_MIN_LENGTH) {
        AppText.Error.PASSWORD_ERROR.text(ctx)
    } else null
}

fun validateUsername(ctx: Context, username: String): String? {
    val lettersCount = username.count { it.isLetter() }
    return if (lettersCount < AppText.Integers.USERNAME_MIN_LENGTH) {
        AppText.Error.USERNAME_ERROR.text(ctx)
    } else null
}

fun validatePhone(ctx: Context, phone: String): String? {
    return if (!phone.matches(Regex("^\\+?[0-9]{10,15}$"))) {
        AppText.Error.PHONE_ERROR.text(ctx)
    } else null
}