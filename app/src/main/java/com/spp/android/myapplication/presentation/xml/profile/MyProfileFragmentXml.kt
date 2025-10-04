package com.spp.android.myapplication.presentation.xml.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.MyProfilePageBinding
import com.spp.android.myapplication.presentation.xml.tabs.MainTabsFragment
import com.spp.android.myapplication.presentation.util.extensions.ValidationUtils.parseNameFromEmail


class MyProfileFragmentXml : Fragment() {

    private var _binding: MyProfilePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userAvatar.setImageResource(R.drawable.profile_avatar)

        val email = activity?.intent?.getStringExtra("email").orEmpty()
        binding.userName.text = parseNameFromEmail(if (email.isBlank()) "User" else email)

        binding.editProfileBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }
        binding.viewMyContactsBtn.setOnClickListener {
            (parentFragment as? MainTabsFragment)?.switchToContacts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}