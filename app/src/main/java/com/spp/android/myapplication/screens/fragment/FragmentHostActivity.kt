package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.spp.android.myapplication.nav.ContactDetailRoute
import com.spp.android.myapplication.nav.ContactsRoute
import com.spp.android.myapplication.nav.MainRoute
import com.spp.android.myapplication.screens.fragment.contact.ContactDetailFragment

class FragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val container = FragmentContainerView(this).apply { id = View.generateViewId() }
        setContentView(container)

        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .replace(container.id, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        val navController = navHostFragment.navController

        val navGraph = navController.createGraph(startDestination = MainRoute) {
            fragment<MainFragment, MainRoute> { }
            fragment<ContactsFragment, ContactsRoute> { }
            fragment<ContactDetailFragment, ContactDetailRoute> { }
        }

        navController.graph = navGraph
    }
}