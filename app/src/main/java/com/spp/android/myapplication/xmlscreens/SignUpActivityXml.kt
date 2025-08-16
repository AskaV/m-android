package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.SignUpPageBinding
import com.spp.android.myapplication.xmlscreens.fragment.contacts.ContactsFragmentActivityXml
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import kotlinx.coroutines.launch


class SignUpActivityXml : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivityXml::class.java))
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
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
                    val intent = Intent(
                        this@SignUpActivityXml,
                        ContactsFragmentActivityXml::class.java
                    ).apply {
                        putExtra("email", emailText)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                }
            }
        }
    }
}