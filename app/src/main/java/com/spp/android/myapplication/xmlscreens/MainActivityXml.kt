package com.spp.android.myapplication.xmlscreens

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.R

class MainActivityXml : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_page)
        val avatarImage = findViewById<ImageView>(R.id.user_avatar)
        avatarImage.setImageResource(R.drawable.profile_avatar)

        val email = intent.getStringExtra("email")
        val userName = parseNameFromEmail(email ?: "User")
        val userNameTextView = findViewById<TextView>(R.id.user_name)

        userNameTextView.text = userName

        findViewById<Button>(R.id.editProfileBtn).setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.viewMyContactsBtn).setOnClickListener {
            Toast.makeText(this, "View Contacts clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseNameFromEmail(email: String): String {
        return email.substringBefore("@")
            .split(".", "_", "-")
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
    }
}