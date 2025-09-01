package com.spp.android.myapplication.ui.screens.fragment.contact

fun Contact.getAvatarTransitionName(index: Int): String =
    "avatar_${index}_${name.replace(" ", "_")}" //todo move in Contact scope

data class Contact( //todo data
    val name: String,
    val position: String,
    val avatarUrl: String
) {

}