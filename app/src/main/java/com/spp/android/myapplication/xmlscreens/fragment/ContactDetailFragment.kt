package com.spp.android.myapplication.xmlscreens.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar

class ContactDetailFragment : Fragment(R.layout.detail_view_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name") ?: "Unknown"
        val position = arguments?.getString("position") ?: ""
        val avatarUrl = arguments?.getString("avatarUrl") ?: ""
        val address = arguments?.getString("address") ?: ""

        view.findViewById<TextView>(R.id.user_name).text = name
        view.findViewById<TextView>(R.id.user_profession).text = position
        view.findViewById<TextView>(R.id.user_address).text = address

        val avatarImage = view.findViewById<ImageView>(R.id.user_avatar)
        avatarImage.loadAvatar(avatarUrl)

        view.findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}