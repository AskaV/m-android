package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.LoginPageBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivityXml : BaseActivity() {

    private lateinit var binding: LoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreInputs(savedInstanceState)

        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivityXml)
            if (!savedEmail.isNullOrBlank()) {
                navigateToMain(savedEmail)
            }
        }
        setupViews()
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val emailField = binding.commonLoginFields.editTextTextEmailAddress

            val allValid = ValidationUtils.validateEmailAndPassword(
                context = this,
                emailField = emailField,
                passwordField = binding.commonLoginFields.editTextTextPassword,
                emailErrorView = binding.commonLoginFields.emailErrorText,
                passwordErrorView = binding.commonLoginFields.passwordErrorText
            )

            if (allValid) {
                val email = emailField.text.toString()
                lifecycleScope.launch {
                    UserPreferences.saveEmail(this@LoginActivityXml, email)
                    navigateToMain(email)
                }
            }
        }

        binding.signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivityXml::class.java))
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
        }
    }

    private fun restoreInputs(savedInstanceState: Bundle?) {
        savedInstanceState?.let { state ->
            binding.commonLoginFields.editTextTextEmailAddress
                .setText(state.getString("email_text", ""))
            binding.commonLoginFields.editTextTextPassword
                .setText(state.getString("password_text", ""))
        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("email", email)
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            "email_text",
            binding.commonLoginFields.editTextTextEmailAddress.text.toString()
        )
        outState.putString(
            "password_text",
            binding.commonLoginFields.editTextTextPassword.text.toString()
        )
    }
}