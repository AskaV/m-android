package com.spp.android.myapplication.presentation.navigation

object Routes {
    const val Auth = "auth"
    const val Login = "auth/login"
    const val SignUp = "auth/signup"
    const val SignUpExtended = "auth/signup_extended"

    const val Home = "home"

    const val EditProfile = "profile/edit"
    const val ContactProfileRoute = "contact/{contactId}"
    fun ContactProfile(id: String) = "contact/$id"
}