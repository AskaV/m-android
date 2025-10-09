package com.spp.android.myapplication.presentation.navigation

sealed class Routes(val route: String) {
    data object Auth : Routes("auth")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object SignUpExtended : Routes("signup_extended")
    data object Home : Routes("home")
    data object EditProfile : Routes("profile/edit")
    data object AddContacts : Routes("add_contacts")

    data object ContactProfile : Routes("contact/{contactId}") {
        const val ARG = "contactId"
        fun create(id: String) = "contact/$id"
    }

    data object AddContactProfile : Routes("contact_profile/{id}") {
        const val ARG = "id"
        fun create(id: String) = "contact_profile/$id"
    }
}