package com.spp.android.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}