package com.spp.android.myapplication.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                AppNavGraph()
            }
        }
    }
}
