package com.spp.android.myapplication.presentation.utils

fun parseNameFromEmail(email: String): Pair<String, String> {
    val username = email.substringBefore("@")
    val parts = username.split(".", "_", "-", limit = 2)
    val first = parts.getOrNull(0)?.replaceFirstChar { it.uppercase() }.orEmpty()
    val last = parts.getOrNull(1)?.replaceFirstChar { it.uppercase() }.orEmpty()
    return first to last
}
