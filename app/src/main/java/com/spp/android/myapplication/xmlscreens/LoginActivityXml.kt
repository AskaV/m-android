package com.spp.android.myapplication.xmlscreens

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.LoginPageBinding
import com.spp.android.myapplication.xmlscreens.util.extensions.LoginViewModel
import com.spp.android.myapplication.xmlscreens.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivityXml : BaseActivity() {
    private lateinit var binding: LoginPageBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()

        lifecycleScope.launchWhenStarted {
            viewModel.email.collect { text ->
                val et = binding.commonLoginFields.editTextTextEmailAddress
                if (et.text.toString() != text) et.setText(text)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.password.collect { text ->
                val et = binding.commonLoginFields.editTextTextPassword
                if (et.text.toString() != text) et.setText(text)
            }
        }
        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivityXml)
            if (!savedEmail.isNullOrBlank()) {
                navigateToMain(savedEmail)
            }
        }
    }

    private fun setupViews() = with(binding){
        val fields = commonLoginFields

        fields.editTextTextEmailAddress.doAfterTextChanged {
            viewModel.setEmail(it?.toString().orEmpty())
        }
        fields.editTextTextPassword.doAfterTextChanged {
            viewModel.setPassword(it?.toString().orEmpty())
        }

        loginButton.setOnClickListener {
            val allValid = ValidationUtils.validateEmailAndPassword(
                context = this@LoginActivityXml,
                emailField = fields.editTextTextEmailAddress,
                passwordField = fields.editTextTextPassword,
                emailErrorView = fields.emailErrorText,
                passwordErrorView = fields.passwordErrorText
            )
            if (allValid) {
                val email = viewModel.email.value
                lifecycleScope.launch {
                    UserPreferences.saveEmail(this@LoginActivityXml, email)
                    navigateToMain(email)
                }
            }
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this@LoginActivityXml, SignUpActivityXml::class.java))
            overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            putExtra(getString(R.string.extra_email), email)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
        finish()
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