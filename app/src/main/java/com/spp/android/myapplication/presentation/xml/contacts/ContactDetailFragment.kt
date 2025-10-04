package com.spp.android.myapplication.presentation.xml.contacts

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.spp.android.myapplication.databinding.DetailViewPageBinding
import com.spp.android.myapplication.presentation.util.extensions.loadAvatar

class ContactDetailFragment : Fragment() {

    private var _binding: DetailViewPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailViewPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ContactDetailFragmentArgs by navArgs()
        val name = args.contactName
        val position = args.position
        val avatarUrl = args.avatarUrl
        val address = args.address
        val tn = args.transitionName

        binding.userName.text = name
        binding.userProfession.text = position
        binding.userAddress.text = address

        ViewCompat.setTransitionName(binding.userAvatar, tn)

        postponeEnterTransition()
        binding.userAvatar.loadAvatar(avatarUrl)
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}