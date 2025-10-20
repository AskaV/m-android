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
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactScreen
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsScreen
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsScreen
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileScreen
import com.spp.android.myapplication.presentation.texts.AppText

object NavKeys {
    const val EMAIL = "email"
    const val PROFILE_UPDATED = "result_profile_updated"
    const val USER_EMAIL = "result_user_email"
    const val USER_NAME = "result_user_name"
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
                onForgotPassword = { Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show() },
                onNavigateHome = {
                    email -> navController.currentBackStackEntry?.savedStateHandle?.set(NavKeys.USER_EMAIL, email)
                    navController.navigate(Routes.Home.route) { launchSingleTop = true } },
                onNavigateToRegister = { navController.navigate(Routes.SignUp.route) })
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                onOpenGoogle = { Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show() },
                onNavigateToLogin = { navController.navigate(Routes.Login.route) { launchSingleTop = true } },
                onNavigateToExtended = {
                    email -> val encoded = java.net.URLEncoder.encode(email, "utf-8")
                    navController.navigate(Routes.SignUpExtended.route + "?${NavKeys.EMAIL}=$encoded") {
                        launchSingleTop = true
                    }
            })
        }

        composable(
            route = Routes.SignUpExtended.route + "?${NavKeys.EMAIL}={${NavKeys.EMAIL}}",
            arguments = listOf(navArgument(NavKeys.EMAIL) { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val emailFromSignUp = backStackEntry.arguments?.getString(NavKeys.EMAIL)

            SignUpExtendedScreen(
                onBack = { navController.popBackStack() },
                onNavigateHome = { email, username ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(NavKeys.USER_EMAIL, email)
                    navController.currentBackStackEntry?.savedStateHandle?.set(NavKeys.USER_NAME, username)
                    navController.navigate(Routes.Home.route) { launchSingleTop = true }
                },
                prefillEmail = emailFromSignUp
            )
        }

        composable(Routes.Home.route) {

            navController.currentBackStackEntry?.savedStateHandle?.set(
                NavKeys.PROFILE_UPDATED, true
            )
            val tabsController = remember { HomeTabsController() }

            val emailFromAuth =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>(NavKeys.USER_EMAIL)
            val nameFromAuth =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>(NavKeys.USER_NAME)

            HomeTabs(controller = tabsController, profile = {
                MyProfileScreen(
                    externalEmail = emailFromAuth,
                    externalName = nameFromAuth,
                    onNavigateContacts = { tabsController.goTo(HomeTab.Contacts) },
                    onNavigateEdit = { navController.navigate(Routes.EditProfile.route) },
                    onNavigateAuth = { navController.navigate(Routes.Login.route) { popUpTo(0) } })
            }, contacts = {
                ContactsScreen(
                    onBack = { tabsController.goTo(HomeTab.Profile) },
                    onOpenSearch = { Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show() },
                    onOpenAddContact = { navController.navigate(Routes.AddContact.route) { launchSingleTop = true } },
                    onOpenContactProfile = { id -> navController.navigate(Routes.ContactProfile.create(id)) })
            })
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onDone = { navController.previousBackStackEntry?.savedStateHandle?.set(NavKeys.PROFILE_UPDATED, true)
                navController.popBackStack()
            })
        }

        composable(
            route = Routes.ContactProfile.route,
            arguments = listOf(
                navArgument(Routes.ContactProfile.ARG) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val contactId =
                backStackEntry.arguments?.getString(Routes.ContactProfile.ARG) ?: return@composable

            ContactProfileScreen(
                contactId = contactId,
                onBack = { navController.popBackStack() },
                onOpenChat = { /* TODO no-op */ })
        }

        composable(Routes.AddContacts.route) {
            AddContactsScreen(
                onBack = { navController.popBackStack() },
                onOpenSearch = { /* TODO no-op */ },
                onOpenProfile = { id -> navController.navigate(Routes.AddContactProfile.create(id)) })
        }

        composable(Routes.AddContact.route) {
            AddContactScreen(
                onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.AddContactProfile.route,
            arguments = listOf(
                navArgument(Routes.AddContactProfile.ARG) {
                    type = NavType.StringType
                })
        ) { backStack ->
            val id =
                backStack.arguments?.getString(Routes.AddContactProfile.ARG) ?: return@composable

            AddContactProfileScreen(
                contactId = id,
                onBack = { navController.popBackStack() })
        }
    }
}