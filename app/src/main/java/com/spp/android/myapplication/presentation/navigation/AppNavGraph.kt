package com.spp.android.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.spp.android.myapplication.presentation.feature.auth.login.LoginScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.base.SignUpScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.extended.SignUpExtendedScreen
import com.spp.android.myapplication.presentation.feature.contacts.ContactsScreen
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileRoute
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Auth
    ) {
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
            composable(Routes.SignUp) {
                SignUpScreen(
                    onOpenGoogle = {/* TODO: add Google Sign-In logic */ },
                    onNavigateToLogin = {
                        navController.navigate(Routes.Login) { launchSingleTop = true }
                    },
                    onNavigateToExtended = {
                        navController.navigate(Routes.SignUpExtended)
                    }
                )
            }
            composable(Routes.SignUpExtended) {
                SignUpExtendedScreen(
                    onBack = { navController.popBackStack() },
                    onOpenAvatarPicker = { /* TODO */ },
                    onNavigateHome = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Auth) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Routes.Home) {
            HomeTabs(
                profile = {
                    MyProfileScreen(
                        onNavigateContacts = { },
                        onNavigateEdit = { navController.navigate(Routes.EditProfile) },
                        onNavigateAuth = {
                            navController.navigate(Routes.Auth) {
                                popUpTo(0)
                            }
                        }
                    )
                },
                contacts = {
                    ContactsScreen(
                        onBack = {},
                        onOpenSearch = { /* TODO open search screen */ },
                        onOpenAddContacts = { /* TODO open "add contacts" flow */ },
                        onOpenContactProfile = { id ->
                            navController.navigate(Routes.ContactProfile(id))
                        }
                    )
                }
            )
        }
        composable(Routes.EditProfile) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onDone = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ContactProfileRoute,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = requireNotNull(backStackEntry.arguments?.getString("contactId"))
            ContactProfileRoute(
                contactId = contactId,
                onBack = { navController.popBackStack() },
                onOpenChat = { /* TODO: navigate to chat */ }
            )
        }
    }
}