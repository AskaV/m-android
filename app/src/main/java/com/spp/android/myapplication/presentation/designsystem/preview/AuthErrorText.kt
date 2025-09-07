package com.spp.android.myapplication.presentation.designsystem.preview

object AuthErrorText {

    object Login {
        const val EMAIL_REQUIRED = "E-mail is required"
        const val EMAIL_FORMAT = "Incorrect E-Mail address"

        const val PASSWORD_REQUIRED = "Password is required"
        const val PASSWORD_SHORT = "Your password must include a minimum of 8 characters."
    }

    object Register {
        const val USERNAME_REQUIRED = "User name is required"
        const val USERNAME_MIN = "User name must contain at least 3 letters"

        const val PHONE_MIN = "Phone must be at least 10 digits long"
    }
}