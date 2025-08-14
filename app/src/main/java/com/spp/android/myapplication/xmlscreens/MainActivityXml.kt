package com.spp.android.myapplication.xmlscreens

import android.os.Bundle
import android.widget.Toast
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.MyProfilePageBinding

class MainActivityXml : BaseActivity() {

    private lateinit var binding: MyProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MyProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userAvatar.setImageResource(R.drawable.profile_avatar)

        val email = intent.getStringExtra("email").orEmpty()
        binding.userName.text = parseNameFromEmail(if (email.isBlank()) "User" else email)

        binding.editProfileBtn.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }
        binding.viewMyContactsBtn.setOnClickListener {
            Toast.makeText(this, "View Contacts clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseNameFromEmail(email: String): String {
        return email.substringBefore("@")
            .split(".", "_", "-")
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
    }
}