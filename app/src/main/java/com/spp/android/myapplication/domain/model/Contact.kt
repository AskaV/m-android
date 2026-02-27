package com.spp.android.myapplication.domain.model

data class Contact(
    val id: Int,
    val name: String,
    val subtitle: String,
    val avatarUrl: String? = null,
    val transitionName: String? = "",
)
