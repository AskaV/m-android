package com.spp.android.myapplication.xmlscreens

import android.app.ActivityOptions
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
            val intent = Intent(this, SignUpActivityXml::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.scale_in,
                R.anim.scale_out
            )
            startActivity(intent, options.toBundle())
        }
    }

    private fun restoreInputs(savedInstanceState: Bundle?) {
        savedInstanceState?.let { state ->
            binding.commonLoginFields.editTextTextEmailAddress.text.toString()

            binding.commonLoginFields.editTextTextPassword.text.toString()

        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(getString(R.string.extra_email), email)
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            getString(R.string.key_email),
            binding.commonLoginFields.editTextTextEmailAddress.text.toString()
        )
        outState.putString(
            getString(R.string.key_password),
            binding.commonLoginFields.editTextTextPassword.text.toString()
        )
    }
}