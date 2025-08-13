package com.spp.android.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.screens.contacts.ContactsScreen
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
//import com.spp.android.myapplication.xmlscreens.contacts.ContactsActivityXml
import kotlinx.coroutines.launch

class StartSelection : ComponentActivity() {

    companion object {
        /** If true – use Compose version, if false – XML version. */
        const val USE_COMPOSE = true
        const val TEST_MODE = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val themePref = prefs.getString("theme_pref", "system") ?: "system"
        prefs.edit().putString("theme_pref", "system").apply()

        if (!USE_COMPOSE) {
            when (themePref) {
                "light" -> setTheme(R.style.Theme_MyApplication)
                "dark" -> setTheme(R.style.Theme_MyApplication_Dark)
                "colored" -> setTheme(R.style.Theme_MyApplication_Colored)
                "system" -> setTheme(R.style.Theme_MyApplication)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (TEST_MODE) {
            lifecycleScope.launch {
                UserPreferences.saveEmail(this@StartSelection, "")
                launchApp(themePref)
            }
        } else {
            launchApp(themePref)
        }
    }

    private fun launchApp(themePref: String) {
        if (USE_COMPOSE) {
            setContent {
                MyApplicationTheme(themePref = themePref) {
                    //LoginScreen(onValidLogin = {})
                    ContactsScreen(onValidLogin = {})
                }
            }
        } else {
            //startActivity(Intent(this, LoginActivityXml::class.java))
//            startActivity(Intent(this, ContactsActivityXml::class.java))
            finish()
        }
    }
}