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
import androidx.navigation.fragment.navArgs


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

        val args: ContactDetailFragmentArgs by navArgs()
        val name = args.contactName
        val position = args.position
        val avatarUrl = args.avatarUrl
        val address = args.address
        val tn = args.transitionName

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