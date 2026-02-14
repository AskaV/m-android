package com.spp.android.myapplication.xmlscreens

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.LoginPageBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.LoginViewModel
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivityXml : BaseActivity() {

    private lateinit var binding: LoginPageBinding
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            vm.email.collect { text ->
                if (binding.commonLoginFields.editTextTextEmailAddress.text.toString() != text) {
                    binding.commonLoginFields.editTextTextEmailAddress.setText(text)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            vm.password.collect { text ->
                if (binding.commonLoginFields.editTextTextPassword.text.toString() != text) {
                    binding.commonLoginFields.editTextTextPassword.setText(text)
                }
            }
        }

        binding.commonLoginFields.editTextTextEmailAddress.doOnTextChanged { text, _, _, _ ->
            vm.setEmail(text?.toString().orEmpty())
        }
        binding.commonLoginFields.editTextTextPassword.doOnTextChanged { text, _, _, _ ->
            vm.setPassword(text?.toString().orEmpty())
        }

        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivityXml)
            if (!savedEmail.isNullOrBlank()) {
                navigateToMain(savedEmail)
            }
        }

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            loginButton.setOnClickListener {
                val emailField = commonLoginFields.editTextTextEmailAddress

                val allValid = ValidationUtils.validateEmailAndPassword(
                    context = this@LoginActivityXml,
                    emailField = emailField,
                    passwordField = commonLoginFields.editTextTextPassword,
                    emailErrorView = commonLoginFields.emailErrorText,
                    passwordErrorView = commonLoginFields.passwordErrorText
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
                val intent = Intent(this@LoginActivityXml, SignUpActivityXml::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    this@LoginActivityXml,
                    R.anim.scale_in,
                    R.anim.scale_out
                )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(getString(R.string.extra_email), email)
        }
        startActivity(intent)
    }
}