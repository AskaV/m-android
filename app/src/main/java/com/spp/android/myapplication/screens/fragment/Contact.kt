package com.spp.android.myapplication.screens.fragment

import com.spp.android.myapplication.screens.contacts.Contact

fun Contact.getAvatarTransitionName(index: Int): String =
    "avatar_${index}_${name.replace(" ", "_")}"