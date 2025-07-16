package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.spp.android.myapplication.screens.contacts.ContactsProfileScreen
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

class ContactDetailFragment : Fragment() {

    private val mockName = "Freddy Harris"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme {
                    ContactsProfileScreen(userName = mockName)
                }
            }
        }
    }
}