package com.spp.android.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.spp.android.myapplication.presentation.feature.auth.login.LoginScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Auth  // для быстрого теста можно поменять тут
    ) {
        // --- AUTH graph ---
        navigation(startDestination = Routes.Login, route = Routes.Auth) {
            composable(Routes.Login) {
                LoginScreen(
                    onNavigateHome = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Auth) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.SignUp) }
                )
            }
        }
    }
}