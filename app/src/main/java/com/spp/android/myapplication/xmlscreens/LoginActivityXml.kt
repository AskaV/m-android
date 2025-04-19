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

class LoginActivityXml : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivityXml)
            if (!savedEmail.isNullOrBlank() && savedEmail != "null") {
                navigateToMain(savedEmail)
            } else {
                setContentView(R.layout.login_page)
                setupViews()
            }
        }
    }

    private fun setupViews() {
        val emailField = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordField = findViewById<EditText>(R.id.editTextTextPassword)
        val emailError = findViewById<TextView>(R.id.emailErrorText)
        val passwordError = findViewById<TextView>(R.id.passwordErrorText)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpText = findViewById<TextView>(R.id.signUpText)

        loginButton.setOnClickListener {
            val allValid = ValidationUtils.validateEmailAndPassword(
                context = this,
                emailField = emailField,
                passwordField = passwordField,
                emailErrorView = emailError,
                passwordErrorView = passwordError
            )

            if (allValid) {
                val email = emailField.text.toString()
                lifecycleScope.launch {
                    UserPreferences.saveEmail(this@LoginActivityXml, email)
                    navigateToMain(email)
                }
            }
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivityXml::class.java))
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
        finish()
    }
}