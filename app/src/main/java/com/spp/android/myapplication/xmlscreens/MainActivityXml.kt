package com.spp.android.myapplication.xmlscreens

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.MyProfilePageBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import com.spp.android.myapplication.xmlscreens.util.extensions.showToast

class MainActivityXml : BaseActivity() {
    private lateinit var binding: MyProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userAvatar.setImageResource(R.drawable.profile_avatar)

        val email = intent.getStringExtra("email") ?: ""
        val userName = ValidationUtils.parseNameFromEmail(email)
        binding.userName.text = userName

        binding.editProfileBtn.setOnClickListener {
            showToast(getString(R.string.toast_edit_profile))
        }

        binding.viewMyContactsBtn.setOnClickListener {
            showToast(getString(R.string.toast_view_contacts))
        }
    }


}