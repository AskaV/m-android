package com.spp.android.myapplication.presentation.navigation

object Routes {
    const val Auth = "auth"
    const val Login = "login"
    const val SignUp = "signup"
    const val SignUpExtended = "signup_extended"

    const val Home = "home"
    const val HomeRoute = "$Home?fromSignup={fromSignup}&completed={completed}"
    fun home(fromSignup: Boolean = false, completed: Boolean = false) =
        "$Home?fromSignup=$fromSignup&completed=$completed"

    const val EditProfile = "profile/edit"
    const val ContactProfileRoute = "contact/{contactId}"

    fun ContactProfile(id: String) = "contact/$id"
}