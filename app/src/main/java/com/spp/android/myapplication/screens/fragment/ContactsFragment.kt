package com.spp.android.myapplication.screens.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spp.android.myapplication.screens.contacts.ContactsScreen
import com.spp.android.myapplication.screens.contacts.ContactsViewModel
import com.spp.android.myapplication.ui.theme.MyApplicationTheme

class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme {
                    ContactsScreen(
                        onValidLogin = {},
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}