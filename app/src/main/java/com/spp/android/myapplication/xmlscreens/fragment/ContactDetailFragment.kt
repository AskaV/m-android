package com.spp.android.myapplication.xmlscreens.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.util.extensions.loadAvatar

class ContactDetailFragment : Fragment(R.layout.detail_view_page) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("contactName") ?: "Unknown"
        val position = arguments?.getString("position") ?: ""
        val avatarUrl = arguments?.getString("avatarUrl") ?: ""
        val address = arguments?.getString("address") ?: ""
        val tn = arguments?.getString("transitionName") ?: "avatar_$name"

        view.findViewById<TextView>(R.id.user_name).text = name
        view.findViewById<TextView>(R.id.user_profession).text = position
        view.findViewById<TextView>(R.id.user_address).text = address

        val avatarImage = view.findViewById<ImageView>(R.id.user_avatar)

        ViewCompat.setTransitionName(avatarImage, tn)

        postponeEnterTransition()
        avatarImage.loadAvatar(avatarUrl)
        view.doOnPreDraw { startPostponedEnterTransition() }

        view.findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}