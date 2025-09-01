package com.spp.android.myapplication.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.ui.components.OutlinedBorderButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.ui.screens.fragment.FragmentHostActivity
import com.spp.android.myapplication.ui.components.EmailPasswordForm
import com.spp.android.myapplication.ui.screens.util.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import com.spp.android.myapplication.ui.theme.Transparent
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo same as in StartSelection
        val themePref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            .getString("theme_pref", "system") ?: "system"

        lifecycleScope.launch {
            //todo same as in StartSelection
            val savedEmail = UserPreferences.getEmail(this@LoginActivity)

            //todo move to StartSelection (where  launchApp(themePref)) and rename it to LoginActivity
            if (!savedEmail.isNullOrBlank()) {
                navigateToMain(savedEmail)
            } else {
                setContentWithLogin(themePref)
            }
        }
    }

    private fun setContentWithLogin(themePref: String) {
        setContent {
            MyApplicationTheme(themePref = themePref) {
                LoginScreen(onValidLogin = { email -> //todo Navigator?
                    lifecycleScope.launch {  //todo just email -> navigateToMain()
                        UserPreferences.saveEmail(this@LoginActivity, email)
                        navigateToMain(email)
                    }
                })
            }
        }
    }

    private fun navigateToMain(email: String) {
        lifecycleScope.launch {
            UserPreferences.saveEmail(this@LoginActivity, email)

            val intent = Intent(this@LoginActivity, FragmentHostActivity::class.java)

            val options = ActivityOptions.makeCustomAnimation(
                this@LoginActivity,
                R.anim.scale_in,
                R.anim.scale_out
            )

            startActivity(intent, options.toBundle())
            finish()
        }
    }

    //todo decompose!!!
    @Preview(
        showBackground = true,
        name = "LoginScreenPreview",
        uiMode = Configuration.UI_MODE_NIGHT_NO,  //UI_MODE_NIGHT_YES,
        //todo just Pixel
        device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"
    )
    @Composable
    fun LoginScreenPreview() {
        MyApplicationTheme(themePref = "system") { // colored or system
            LoginScreen(onValidLogin = {})
        }
    }

    @Composable
    fun LoginScreen(
        modifier: Modifier = Modifier,
        onValidLogin: (String) -> Unit
    ) {
        val context = LocalContext.current

        //todo hello-text not as in design, inputs not as in design,

        //todo viewmodel. Read about MVVM (and Hilt!) and Unidirectional flow
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var rememberMe by rememberSaveable { mutableStateOf(false) }
        var emailError by rememberSaveable { mutableStateOf<String?>(null) }
        var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    start = dimensionResource(id = R.dimen.spacer_medium),
                    end = dimensionResource(id = R.dimen.spacer_medium),
                    bottom = dimensionResource(id = R.dimen.spacer_medium)
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_margin_top_large)))
                Text(
                    text = stringResource(id = R.string.hello),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_margin_top_small)))
                Text(
                    text = stringResource(id = R.string.enter_email_password),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_margin_top_small)))

                EmailPasswordForm(
                    showSubmitButton = false,
                    email = email,
                    onEmailChange = {
                        email = it
                        emailError = null
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                        passwordError = null
                    },
                    emailError = emailError,
                    passwordError = passwordError,
                    onSubmit = {
                        //todo move logic in VM
                        val emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        val passwordValid =
                            password.length >= context.resources.getInteger(R.integer.password_length)

                        var valid = true
                        if (!emailValid) {
                            emailError = context.getString(R.string.email_error_text)
                            valid = false
                        }
                        if (!passwordValid) {
                            passwordError = context.getString(R.string.password_error_text)
                            valid = false
                        }
                        if (valid) onValidLogin(email) //todo {}
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_small)))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.spacer_extra_small)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //todo decompose
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.checkbox_frame_size))
                                .border(
                                    width = dimensionResource(id = R.dimen.checkbox_border_width),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.checkbox_corner_radius))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Transparent,
                                    uncheckedColor = Transparent,
                                    checkmarkColor = MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier.size(dimensionResource(id = R.dimen.checkbox_inner_size))
                            )
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacer_small)))
                        Text(
                            text = stringResource(R.string.remember_me),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedBorderButton(
                    text = stringResource(R.string.login).uppercase(),
                    onClick = {
                        //todo VM
                        val emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        val passwordValid =
                            password.length >= context.resources.getInteger(R.integer.password_length)

                        var valid = true
                        if (!emailValid) {
                            emailError = context.getString(R.string.email_error_text)
                            valid = false
                        }
                        if (!passwordValid) {
                            passwordError = context.getString(R.string.password_error_text)
                            valid = false
                        }
                        if (valid) onValidLogin(email) //todo {}
                    },
                    borderColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_small)))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.spacer_medium))
                ) {
                    Text(
                        text = stringResource(id = R.string.dont_have_account),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacer_small)))
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable {

                            //todo or start with argument (as onValidLogin) or start manually both (this and onValidLogin)
                            val options = ActivityOptions.makeCustomAnimation(
                                context,
                                R.anim.scale_in,
                                R.anim.scale_out
                            )
                            context.startActivity(
                                Intent(context, SignUpActivity::class.java),
                                options.toBundle()
                            )
                        }
                    )
                }
            }
        }
    }
}