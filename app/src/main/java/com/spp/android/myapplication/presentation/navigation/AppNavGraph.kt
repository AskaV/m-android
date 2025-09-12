package com.spp.android.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.spp.android.myapplication.presentation.feature.auth.login.LoginScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.base.SignUpScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.extended.SignUpExtendedScreen
import com.spp.android.myapplication.presentation.feature.contacts.add.AddContactsScreen
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsScreen
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.edit.EditProfileScreenContent
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileScreen
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileViewModel

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
                    onNavigateHome = {
                        navController.navigate(Routes.home(fromSignup = true)) {
                            popUpTo(Routes.Auth) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(
            route = Routes.HomeRoute,
            arguments = listOf(navArgument("fromSignup") { defaultValue = "false" })
        ) { backStackEntry ->
            val vm: MyProfileViewModel = hiltViewModel()

            val fromSignup = backStackEntry.arguments?.getString("fromSignup") == "true"
            val completed = backStackEntry.arguments?.getString("completed") == "true"

            LaunchedEffect(fromSignup, completed) {
                if (fromSignup) vm.onEvent(MyProfileContract.Event.SignUpFinished)
                if (completed) vm.onEvent(MyProfileContract.Event.MarkCompleted)
            }
            val tabsController = remember { HomeTabsController() }

            HomeTabs(
                controller = tabsController,
                profile = {
                    MyProfileScreen(
                        onNavigateContacts = { tabsController.goTo(HomeTab.Contacts) },
                        onNavigateEdit = { navController.navigate(Routes.EditProfile) },
                        onNavigateAuth = {
                            navController.navigate(Routes.Auth) { popUpTo(0) }
                        }
                    )
                },
                contacts = {
                    ContactsScreen(
                        onBack = { },
                        onOpenSearch = { /* ... */ },
                        onOpenAddContacts = {
                            navController.navigate(Routes.AddContacts) {
                                launchSingleTop = true
                            }
                        },
                        onOpenContactProfile = { id ->
                            navController.navigate(Routes.ContactProfile(id))
                        }
                    )
                }
            )
        }
        composable(Routes.EditProfile) {
            val vm: MyProfileViewModel = hiltViewModel()

            EditProfileScreenContent(
                onBack = { navController.navigateUp() },
                onSave = { username, career, phone, address, birthdate ->
                    vm.onEvent(
                        MyProfileContract.Event.ProfileSaved(
                            username, career, phone, address, birthdate
                        )
                    )
                    navController.navigate(Routes.home(fromSignup = false, completed = true)) {
                        popUpTo(Routes.Home) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.ContactProfileRoute,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable

            ContactProfileScreen(
                contactId = contactId,
                onBack = { navController.popBackStack() },
                onOpenChat = { }
            )
        }

        composable(Routes.AddContacts) {
            AddContactsScreen(
                onBack = { navController.popBackStack() },
                onOpenSearch = { /* ... */ },
                onOpenProfile = { id ->
                    navController.navigate(Routes.addContactProfile(id))
                }
            )
        }
        composable(Routes.AddContactProfile) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            AddContactProfileScreen(
                contactId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}