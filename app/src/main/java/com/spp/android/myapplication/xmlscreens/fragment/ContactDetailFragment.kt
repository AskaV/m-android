package com.spp.android.myapplication.xmlscreens.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar

class ContactDetailFragment : Fragment(R.layout.detail_view_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ContactDetailFragmentArgs by navArgs()

        view.findViewById<TextView>(R.id.user_name).text = args.contactName
        view.findViewById<TextView>(R.id.user_profession).text = args.position
        view.findViewById<TextView>(R.id.user_address).text = args.address

        val avatarImage = view.findViewById<ImageView>(R.id.user_avatar)
        avatarImage.loadAvatar(args.avatarUrl)

        view.findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}