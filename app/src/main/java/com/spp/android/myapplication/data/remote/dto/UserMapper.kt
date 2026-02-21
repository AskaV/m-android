package com.spp.android.myapplication.data.remote.dto

import com.spp.android.myapplication.domain.model.Contact

fun UserDto.toContact(): Contact {
    return Contact(
        id = id,
        name = name ?: "",
        subtitle = email ?: ""
    )
}