package com.spp.android.myapplication.presentation.xml.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.ThemePreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val themePref = runBlocking { ThemePreferences.themeFlow(this@BaseActivity).first() }
        applyTheme(themePref)

        super.onCreate(savedInstanceState)
    }

    private fun applyTheme(themePref: String) {
        when (themePref) {
            "light" -> setTheme(R.style.Theme_MyApplication)
            "dark" -> setTheme(R.style.Theme_MyApplication_Dark)
            "colored" -> setTheme(R.style.Theme_MyApplication_Colored)
            "system" -> setTheme(R.style.Theme_MyApplication)
            else -> setTheme(R.style.Theme_MyApplication)
        }
    }
}
