package com.spp.android.myapplication.screens.fragment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val position: String,
    val avatarUrl: String
) : Parcelable