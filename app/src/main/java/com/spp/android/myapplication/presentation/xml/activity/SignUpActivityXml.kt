package com.spp.android.myapplication.presentation.xml.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.SignUpPageBinding
import com.spp.android.myapplication.presentation.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class SignUpActivityXml : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInText.setOnClickListener {
            val intent = Intent(this, LoginActivityXml::class.java)
            startActivity(intent, animOptions().toBundle())
            finish()
        }
        binding.registerButton.setOnClickListener {
            val emailField = binding.commonLoginFields.editTextTextEmailAddress
            val passwordField = binding.commonLoginFields.editTextTextPassword

            val emailText = emailField.text?.toString()?.trim().orEmpty()
            val passwordText = passwordField.text?.toString()?.trim().orEmpty()

            val allValid = ValidationUtils.validateEmailAndPassword(
                emailField = emailField,
                passwordField = passwordField,
                emailErrorView = binding.commonLoginFields.emailErrorText,
                passwordErrorView = binding.commonLoginFields.passwordErrorText
            )
            if (!allValid) return@setOnClickListener

            lifecycleScope.launch {
                val isNew = isEmailNew(emailText)
                if (!isNew) {
                    binding.commonLoginFields.emailErrorText.apply {
                        text = getString(R.string.signup_reg_email_msg)
                        visibility = View.VISIBLE
                    }
                    return@launch
                }

                val intent = Intent(
                    this@SignUpActivityXml,
                    SignUpExtendedActivityXml::class.java
                ).apply {
                    putExtra("email", emailText)
                    putExtra("password", passwordText)
                }
                startActivity(intent, animOptions().toBundle())
            }
        }
    }

    private suspend fun isEmailNew(email: String): Boolean {
        // TODO: PoST?
        return true
    }

    private fun animOptions() = ActivityOptions.makeCustomAnimation(
        this, R.anim.scale_in, R.anim.scale_out
    )
}