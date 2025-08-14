package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.SignUpPageBinding
import kotlinx.coroutines.launch


class SignUpActivityXml : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivityXml::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {

            val emailField = binding.commonLoginFields.editTextTextEmailAddress
            val emailText = emailField.text.toString()

            val allValid = ValidationUtils.validateEmailAndPassword(
                context = this,
                emailField = emailField,
                passwordField = binding.commonLoginFields.editTextTextPassword,
                emailErrorView = binding.commonLoginFields.emailErrorText,
                passwordErrorView = binding.commonLoginFields.passwordErrorText
            )

            if (allValid) {
                lifecycleScope.launch {
                    UserPreferences.saveEmail(this@SignUpActivityXml, emailText)
                    val intent = Intent(this@SignUpActivityXml, MainActivityXml::class.java).apply {
                        putExtra("email", emailText)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                    finish()
                }
            }
        }
    }
}