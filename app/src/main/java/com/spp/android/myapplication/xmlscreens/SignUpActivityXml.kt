package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.SignUpPageBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class SignUpActivityXml : BaseActivity() {
    private lateinit var binding: SignUpPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val fields = commonLoginFields

            signInText.setOnClickListener {
                startActivity(Intent(this@SignUpActivityXml, LoginActivityXml::class.java))
                finish()
            }

            loginButton.setOnClickListener {
                val email = fields.editTextTextEmailAddress.text.toString()
                val allValid = ValidationUtils.validateEmailAndPassword(
                    context = this@SignUpActivityXml,
                    emailField = fields.editTextTextEmailAddress,
                    passwordField = fields.editTextTextPassword,
                    emailErrorView = fields.emailErrorText,
                    passwordErrorView = fields.passwordErrorText
                )

                if (allValid) {
                    lifecycleScope.launch {
                        UserPreferences.saveEmail(this@SignUpActivityXml, email)
                        val intent =
                            Intent(this@SignUpActivityXml, MainActivityXml::class.java).apply {
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
}