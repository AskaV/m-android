package com.spp.android.myapplication.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spp.android.myapplication.presentation.feature.auth.login.LoginScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.base.SignUpScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.extended.SignUpExtendedScreen
import com.spp.android.myapplication.presentation.feature.contacts.add.AddContactsScreen
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsScreen
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileScreen
import com.spp.android.myapplication.presentation.texts.AppText

object NavKeys {
    const val PROFILE_UPDATED = "result_profile_updated"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val toastMessage = AppText.OtherInfo.TOAST_CLICKED.text(context)

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onForgotPassword = {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                },
                onNavigateHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(0)
                    }
                }, onNavigateToRegister = {
                    navController.navigate(Routes.SignUp.route)
                })
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                onOpenGoogle = {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToExtended = {
                    navController.navigate(Routes.SignUpExtended.route)
                })
        }

        composable(Routes.SignUpExtended.route) {
            SignUpExtendedScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(0)
                    }
                })
        }

        composable(Routes.Home.route) {

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(NavKeys.PROFILE_UPDATED, true)
            val tabsController = remember { HomeTabsController() }

            HomeTabs(
                controller = tabsController,
                profile = {
                    MyProfileScreen(
                        onNavigateContacts = { tabsController.goTo(HomeTab.Contacts) },
                        onNavigateEdit = { navController.navigate(Routes.EditProfile.route) },
                        onNavigateAuth = {
                            navController.navigate(Routes.Login.route) { popUpTo(0) }
                        })
                },
                contacts = {
                    ContactsScreen(
                        onBack = { /* no-op */ },
                        onOpenSearch = { /* no-op */ },
                        onOpenAddContacts = {
                            navController.navigate(Routes.AddContacts.route) {
                                launchSingleTop = true
                            }
                        },
                        onOpenContactProfile = { id ->
                            navController.navigate(Routes.ContactProfile.create(id))
                        })
                })
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onDone = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NavKeys.PROFILE_UPDATED, true)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.ContactProfile.route, arguments = listOf(
                navArgument(Routes.ContactProfile.ARG) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val contactId =
                backStackEntry.arguments?.getString(Routes.ContactProfile.ARG) ?: return@composable

            ContactProfileScreen(
                contactId = contactId,
                onBack = { navController.popBackStack() },
                onOpenChat = { /* no-op */ })
        }

        composable(Routes.AddContacts.route) {
            AddContactsScreen(
                onBack = { navController.popBackStack() },
                onOpenSearch = { /* no-op */ },
                onOpenProfile = { id ->
                    navController.navigate(Routes.AddContactProfile.create(id))
                })
        }

        composable(
            route = Routes.AddContactProfile.route, arguments = listOf(
                navArgument(Routes.AddContactProfile.ARG) {
                    type = NavType.StringType
                })
        ) { backStack ->
            val id =
                backStack.arguments?.getString(Routes.AddContactProfile.ARG) ?: return@composable

            AddContactProfileScreen(
                contactId = id, onBack = { navController.popBackStack() })
        }
    }
}