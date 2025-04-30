package com.spp.android.myapplication.xmlscreens

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        when (prefs.getString("theme_pref", "system")) {
            "light" -> setTheme(R.style.Theme_MyApplication)
            "dark" -> setTheme(R.style.Theme_MyApplication_Dark)
            "colored" -> setTheme(R.style.Theme_MyApplication_Colored)
            "system" -> setTheme(R.style.Theme_MyApplication)
        }
        super.onCreate(savedInstanceState)
    }
}