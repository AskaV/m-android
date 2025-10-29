package com.spp.android.myapplication.data.contacts

import kotlin.random.Random

class AvatarProvider {

    fun forId(id: Int, current: String?): String {
        if (!current.isNullOrBlank()) return current
        return generateUrl(id)
    }

    fun random(current: String? = null): String {
        if (!current.isNullOrBlank()) return current
        return generateUrl(Random.nextInt())
    }

    private fun generateUrl(seed: Int): String {
        return "https://api.dicebear.com/6.x/adventurer/png?seed=$seed"
    }
}