package com.spp.android.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.data.ThemePreferences
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.presentation.xml.activity.LoginActivityXml
import com.spp.android.myapplication.presentation.xml.activity.MainActivityXml
import com.spp.android.myapplication.presentation.xml.activity.SignUpExtendedActivityXml
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartSelection : ComponentActivity() {
    companion object {
        const val TEST_MODE = true

        enum class StartTarget { AUTH, CONTACTS, SIGNUP_EXTENDED }

        val START_THEME = "system" // "light" | "dark" | "colored" | "system"

        val START_TARGET = StartTarget.AUTH
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val themePref =
            runBlocking {
                ThemePreferences.setTheme(this@StartSelection, START_THEME)
                ThemePreferences.themeFlow(this@StartSelection).first()
            }
        applyTheme(themePref)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (TEST_MODE) {
            lifecycleScope.launch {
                UserPreferences.saveEmail(this@StartSelection, "")
                launchApp()
            }
        } else {
            launchApp()
        }
    }

    private fun launchApp() {
        when (START_TARGET) {
            StartTarget.AUTH -> {
                startActivity(Intent(this, LoginActivityXml::class.java))
            }

            StartTarget.CONTACTS -> {
                startActivity(
                    Intent(this, MainActivityXml::class.java)
                        .putExtra("force_theme", START_THEME)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK),
                )
            }

            StartTarget.SIGNUP_EXTENDED -> {
                startActivity(
                    Intent(this, SignUpExtendedActivityXml::class.java).apply {
                        putExtra("email", "test@example.com")
                        putExtra("password", "Qwerty123!")
                    },
                )
            }
        }
        finish()
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
