package com.spp.android.myapplication.ui.screens.util.extensions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spp.android.myapplication.ui.screens.fragment.ContactsFragment
import com.spp.android.myapplication.ui.screens.fragment.MyProfileFragment

class MainPagerAdapter(host: Fragment) : FragmentStateAdapter(host) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MyProfileFragment()
            else -> ContactsFragment()
        }
}