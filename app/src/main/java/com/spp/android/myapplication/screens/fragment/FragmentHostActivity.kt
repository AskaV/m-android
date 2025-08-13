package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment

class FragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = FragmentContainerView(this).apply {
            id = View.generateViewId()
        }
        setContentView(container)

        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .replace(container.id, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        val navController = navHostFragment.navController
        val navGraph = navController.createGraph(startDestination = "contacts") {
            fragment<ContactsFragment>("contacts")
            fragment<ContactDetailFragment>(
                "contact_detail?name={name}&position={position}&avatarUrl={avatarUrl}&transitionName={transitionName}"
            ) {
                argument("name") { defaultValue = "" }
                argument("position") { defaultValue = "" }
                argument("avatarUrl") { defaultValue = "" }
                argument("transitionName") { defaultValue = "" }
            }
        }

        navController.graph = navGraph
    }
}