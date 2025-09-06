package com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data

object FormsPreviewText {
    const val EMAIL = "example@email.com"
    const val WRONG_EMAIL = "ex"
    const val PASSWORD = "qwerty123"
    const val WRONG_PASSWORD = "123"
    const val USERNAME = "Lucile Alvarado"
    const val WRONG_USERNAME = "Lu"
    const val PHONE = "(264) 654 - 3..."
    const val WRONG_PHONE = "12345"
    const val DOTS = "••••••••"

    object Error {
        const val EMAIL = "Incorrect E-Mail address"
        const val PASSWORD = "Your password must include a minimum of 8 characters."
        const val USERNAME = "User name must contain at least 3 letters"
        const val PHONE = "Phone must be at least 10 digits long"
    }

    object Label {
        const val EMAIL = "E-mail"
        const val PASSWORD = "Password"
        const val USERNAME = "User name"
        const val PHONE = "Mobile phone"
    }

    object GalleryStrings {
        const val OPEN_GALLERY = "Open gallery"
        const val DELETE_PHOTO = "Delete current photo"
        const val CANCEL = "Cancel"
    }
}