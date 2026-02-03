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

        setupBinding()
        setupTextListeners()
        setupViews()

        collectState()
        checkAutoLogin()
    }

    private fun setupBinding() {
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun setupTextListeners() = with(binding.commonLoginFields) {
        editTextTextEmailAddress.doAfterTextChanged { text ->
            viewModel.setEmail(text?.toString().orEmpty())
        }
        editTextTextPassword.doAfterTextChanged { text ->
            viewModel.setPassword(text?.toString().orEmpty())
        }
    }


    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val emailEt = binding.commonLoginFields.editTextTextEmailAddress
                    val passEt = binding.commonLoginFields.editTextTextPassword

                    if (emailEt.text?.toString() != state.email) {
                        emailEt.setText(state.email)
                    }
                    if (passEt.text?.toString() != state.password) {
                        passEt.setText(state.password)
                    }
                }
            }
        }
    }

    private fun checkAutoLogin() {
        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivityXml)
            if (!savedEmail.isNullOrBlank()) {
                navigateToMain(savedEmail)
            }
        }
    }

    private fun setupViews() = with(binding) {
        loginButton.setOnClickListener {
            val emailField = commonLoginFields.editTextTextEmailAddress

            val allValid = ValidationUtils.validateEmailAndPassword(
                emailField = emailField,
                passwordField = commonLoginFields.editTextTextPassword,
                emailErrorView = commonLoginFields.emailErrorText,
                passwordErrorView = commonLoginFields.passwordErrorText
            )

            if (allValid) {
                val email = viewModel.state.value.email
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

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivityXml::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(getString(R.string.extra_email), email)
        }
        startActivity(intent)
    }
}