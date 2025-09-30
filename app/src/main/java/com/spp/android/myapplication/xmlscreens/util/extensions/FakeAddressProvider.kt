package com.spp.android.myapplication.xmlscreens.util.extensions

object FakeAddressProvider {
    private val pool = listOf(
        "New York, USA", "Berlin, Germany", "Kyiv, Ukraine", "Warsaw, Poland",
        "Barcelona, Spain", "Paris, France", "Prague, Czechia", "Rome, Italy",
        "Tokyo, Japan", "Toronto, Canada"
    )

    fun forName(name: String): String =
        pool[kotlin.math.abs(name.hashCode()) % pool.size]
}