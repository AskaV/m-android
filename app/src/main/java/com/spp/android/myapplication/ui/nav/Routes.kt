package com.spp.android.myapplication.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data object ContactsRoute

@Serializable
data class ContactDetailRoute(
    val name: String,
    val position: String,
    val avatarUrl: String,
    val transitionName: String = ""
)

@Serializable
data object MainRoute