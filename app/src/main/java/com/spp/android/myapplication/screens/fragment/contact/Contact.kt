package com.spp.android.myapplication.screens.fragment.contact

fun Contact.getAvatarTransitionName(index: Int): String =
    "avatar_${index}_${name.replace(" ", "_")}"

data class Contact(
    val name: String,
    val position: String,
    val avatarUrl: String
)