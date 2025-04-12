package com.spp.android.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.spp.android.myapplication.screens.LoginScreen
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

class StartSelection : ComponentActivity() {

    companion object {
        /** If true – use Compose version, if false – XML version. */
        const val USE_COMPOSE = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (USE_COMPOSE) {
                setContent {
                    MyApplicationTheme {
                        LoginScreen(onValidLogin = {})
                    }
                }
            } else {
                // ToDO start xml version
            }
        }
    }
}
