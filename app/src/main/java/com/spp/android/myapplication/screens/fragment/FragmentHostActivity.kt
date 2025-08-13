package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.spp.android.myapplication.nav.ContactDetailRoute
import com.spp.android.myapplication.nav.ContactsRoute

class FragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = FragmentContainerView(this).apply { id = View.generateViewId() }
        setContentView(container)

        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .replace(container.id, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        val navController = navHostFragment.navController

        val navGraph = navController.createGraph(startDestination = ContactsRoute) {
            fragment<ContactsFragment, ContactsRoute> { }
            fragment<ContactDetailFragment, ContactDetailRoute> { }
        }

        navController.graph = navGraph
    }
}