package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import kotlinx.coroutines.launch

class SignUpActivityXml : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)

        val emailField = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordField = findViewById<EditText>(R.id.editTextTextPassword)

        val emailError = findViewById<TextView>(R.id.emailErrorText)
        val passwordError = findViewById<TextView>(R.id.passwordErrorText)

        val signInText = findViewById<TextView>(R.id.signInText)
        val signUpButton = findViewById<Button>(R.id.loginButton)

        signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivityXml::class.java))
            finish()
        }

        signUpButton.setOnClickListener {
            val email = emailField.text.toString()
            val allValid = ValidationUtils.validateEmailAndPassword(
                context = this,
                emailField = emailField,
                passwordField = passwordField,
                emailErrorView = emailError,
                passwordErrorView = passwordError
            )

            if (allValid) {
                lifecycleScope.launch {
                    UserPreferences.saveEmail(this@SignUpActivityXml, email)
                    val intent = Intent(this@SignUpActivityXml, MainActivityXml::class.java).apply {
                        putExtra("email", email)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                    finish()
                }
            }
        }
    }
}