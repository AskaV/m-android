package com.spp.android.myapplication.data.contacts

data class ContactDetails(
    val id: Int,
    val name: String,
    val career: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
)