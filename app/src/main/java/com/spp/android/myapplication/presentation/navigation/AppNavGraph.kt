package com.spp.android.myapplication.presentation.navigation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.spp.android.myapplication.presentation.feature.auth.login.LoginScreen
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
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
import kotlinx.coroutines.flow.Flow

object NavKeys {
    const val EMAIL = "email"
    const val PROFILE_UPDATED = "result_profile_updated"
    const val USER_EMAIL = "result_user_email"
    const val USER_NAME = "result_user_name"
}

@Composable
fun AppNavGraph(intentFlow: Flow<Intent>) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val toastMessage = AppText.OtherInfo.TOAST_CLICKED.text(context)

    val resolverVm: DeepLinkResolverViewModel = hiltViewModel()

    LaunchedEffect(navController) {
        intentFlow.collect { intent ->
            val id = parseContactId(intent)

            if (id != null) {
                val inMy = resolverVm.isInMyContacts(id)

                navController.navigate(Routes.Home.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }

                val target = if (inMy) {
                    Routes.ContactProfile.create(id)
                } else {
                    Routes.AddContactProfile.create(id)
                }

                navController.navigate(target) {
                    launchSingleTop = true
                }

                return@collect
            }

            navController.handleDeepLink(intent)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onForgotPassword = { showToast(context, toastMessage) },
                onNavigateHome = { email ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        NavKeys.USER_EMAIL,
                        email,
                    )
                    navController.navigate(Routes.Home.route) { launchSingleTop = true }
                },
                onNavigateToRegister = { navController.navigate(Routes.SignUp.route) },
            )
        }

        composable(Routes.SignUp.route) { backStackEntry ->
            val viewModel: SignUpViewModel = hiltViewModel(backStackEntry)

            SignUpScreen(
                viewModel = viewModel,
                onOpenGoogle = { showToast(context, toastMessage) },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToExtended = { email ->
                    val encoded = java.net.URLEncoder.encode(email, "utf-8")
                    navController.navigate(Routes.SignUpExtended.route + "?${NavKeys.EMAIL}=$encoded") {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = Routes.SignUpExtended.route + "?${NavKeys.EMAIL}={${NavKeys.EMAIL}}",
            arguments = listOf(
                navArgument(NavKeys.EMAIL) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) { backStackEntry ->
            val emailFromSignUp = backStackEntry.arguments?.getString(NavKeys.EMAIL)

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.SignUp.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            SignUpExtendedScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateHome = { email, username ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        NavKeys.USER_EMAIL,
                        email,
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        NavKeys.USER_NAME,
                        username,
                    )
                    navController.navigate(Routes.Home.route) { launchSingleTop = true }
                },
                prefillEmail = emailFromSignUp,
            )
        }

        composable(Routes.Home.route) {
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
                    onNavigateAuth = { navController.navigate(Routes.Login.route) { popUpTo(0) } },
                    navController = navController,
                )
            }, contacts = {
                ContactsScreen(
                    onBack = { tabsController.goTo(HomeTab.Profile) },
                    onOpenSearch = { showToast(context, toastMessage) },

                    onOpenAddContact = {
                        navController.navigate(Routes.AddContact.route) { launchSingleTop = true }
                    },

                    onOpenAddContacts = {
                        navController.navigate(Routes.AddContacts.route) { launchSingleTop = true }
                    },

                    onOpenContactProfile = { id ->
                        navController.navigate(
                            Routes.ContactProfile.create(
                                id
                            )
                        )
                    },
                )
            })
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onDone = { result ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "profile_result",
                        result,
                    )
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Routes.ContactProfile.route,
            arguments = listOf(
                navArgument(Routes.ContactProfile.ARG) {
                    type = NavType.IntType
                },
            ),
        ) { backStackEntry ->
            val contactId =
                backStackEntry.arguments?.getInt(Routes.ContactProfile.ARG) ?: return@composable

            ContactProfileScreen(
                contactId = contactId,
                onBack = { navController.popBackStack() },
                onOpenChat = { showToast(context, toastMessage) },
            )
        }

        composable(Routes.AddContacts.route) {
            AddContactsScreen(

                onBack = { navController.popBackStack() },
                onOpenProfile = { id -> navController.navigate(Routes.AddContactProfile.create(id)) },
            )
        }

        composable(Routes.AddContact.route) {
            AddContactScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.AddContactProfile.route,
            arguments = listOf(
                navArgument(Routes.AddContactProfile.ARG) {
                    type = NavType.IntType
                },
            ),
        ) { backStack ->
            val id = backStack.arguments?.getInt(Routes.AddContactProfile.ARG) ?: return@composable

            AddContactProfileScreen(
                contactId = id,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

private fun parseContactId(intent: Intent): Int? {
    val uri = intent.data ?: return null
    if (intent.action != Intent.ACTION_VIEW) return null
    if (uri.scheme != "myapp" || uri.host != "contact") return null
    return uri.lastPathSegment?.toIntOrNull()
}
fun showToast(
    context: Context,
    message: String,
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
