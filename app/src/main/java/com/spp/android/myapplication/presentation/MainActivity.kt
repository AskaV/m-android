package com.spp.android.myapplication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val intentFlow = MutableSharedFlow<Intent>(extraBufferCapacity = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentFlow.tryEmit(intent)

        setContent {
            MyApplicationTheme {
                AppNavGraph(intentFlow = intentFlow)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intentFlow.tryEmit(intent)
    }
}
