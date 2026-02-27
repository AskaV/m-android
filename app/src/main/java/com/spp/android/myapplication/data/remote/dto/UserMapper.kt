package com.spp.android.myapplication.data.remote.dto

import com.spp.android.myapplication.domain.model.Contact

fun UserDto.toContact(): Contact {
    val url = image?.trim().takeIf { !it.isNullOrBlank() }

    return Contact(
        id = id,
        name = name ?: "",
        subtitle = email,
        avatarUrl = url,
    )
}