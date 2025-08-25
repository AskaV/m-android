package com.spp.android.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import com.spp.android.myapplication.xmlscreens.LoginActivityXml
import kotlinx.coroutines.launch

class StartSelection : ComponentActivity() {

    companion object {
        /** If true – use Compose version, if false – XML version. */
        //const val USE_COMPOSE = true
        const val USE_COMPOSE = false
        const val TEST_MODE = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (TEST_MODE) {
            lifecycleScope.launch {
                //UserPreferences.saveEmail(this@StartSelection, "")
                launchApp()
            }
        } else {
            launchApp()
        }
    }

    private fun launchApp() {
        if (USE_COMPOSE) {
            setContent {
                MyApplicationTheme {
                    //LoginScreen(onValidLogin = {})
                }
            }
        } else {
            startActivity(Intent(this, LoginActivityXml::class.java))
            finish()
        }
    }
}