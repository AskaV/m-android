package com.spp.android.myapplication.presentation.xml.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.FragmentMainTabsBinding
import com.spp.android.myapplication.presentation.xml.contacts.ContactsFragmentXml
import com.spp.android.myapplication.presentation.xml.profile.MyProfileFragmentXml

class MainTabsFragment : Fragment() {

    private var _binding: FragmentMainTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> MyProfileFragmentXml()
                else -> ContactsFragmentXml()
            }
        }

        val titles = listOf(
            getString(R.string.tab_profile), getString(R.string.tab_contacts)
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun switchToContacts() {
        _binding?.viewPager?.currentItem = 1
    }

    fun switchToProfile() {
        _binding?.viewPager?.currentItem = 0
    }
}