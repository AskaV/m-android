package com.spp.android.myapplication.data.contacts

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
@Singleton
class AvatarProvider @Inject constructor() {

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