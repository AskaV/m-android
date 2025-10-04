package com.spp.android.myapplication.presentation.xml.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.databinding.LoginPageBinding
import com.spp.android.myapplication.presentation.util.extensions.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivityXml : BaseActivity() {

    private lateinit var binding: LoginPageBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.commonLoginFields.editTextTextEmailAddress.doAfterTextChanged {
            viewModel.setEmail(it?.toString().orEmpty())
        }
        binding.commonLoginFields.editTextTextPassword.doAfterTextChanged {
            viewModel.setPassword(it?.toString().orEmpty())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { s ->
                    val emailEt = binding.commonLoginFields.editTextTextEmailAddress
                    if (emailEt.text?.toString() != s.email) emailEt.setText(s.email)

                    val passEt = binding.commonLoginFields.editTextTextPassword
                    if (passEt.text?.toString() != s.password) passEt.setText(s.password)
                }
            }
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
                val email = viewModel.state.value.email
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

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(getString(R.string.extra_email), email)
        }
        startActivity(intent)
    }
}