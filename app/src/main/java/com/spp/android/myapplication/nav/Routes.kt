package com.spp.android.myapplication.nav

import kotlinx.serialization.Serializable

@Serializable
data object ContactsRoute // стартовый экран, без аргументов

@Serializable
data class ContactDetailRoute(
    val name: String,
    val position: String,
    val avatarUrl: String,
    val transitionName: String = ""
)