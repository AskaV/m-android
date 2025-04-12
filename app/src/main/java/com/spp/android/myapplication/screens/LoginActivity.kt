package com.spp.android.myapplication.screens

import android.app.ActivityOptions
import android.content.Intent
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.OutlinedBorderButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.ui.components.EmailPasswordForm
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.Blue
import com.spp.android.myapplication.ui.theme.GrayText2
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import com.spp.android.myapplication.ui.theme.Orange
import com.spp.android.myapplication.ui.theme.White
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val savedEmail = UserPreferences.getEmail(this@LoginActivity)

            if (savedEmail != null) {
                navigateToMain(savedEmail)
            } else {
                setContentWithLogin()
            }
        }
    }

    private fun setContentWithLogin() {
        setContent {
            MyApplicationTheme {
                LoginScreen(onValidLogin = { email ->
                    lifecycleScope.launch {
                        UserPreferences.saveEmail(this@LoginActivity, email)
                        navigateToMain(email)
                    }
                })
            }
        }
    }

    private fun navigateToMain(email: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }

        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.scale_in,
            R.anim.scale_out
        )

        startActivity(intent, options.toBundle())
        finish()
    }
}

@Preview(
    showBackground = true,
    name = "SignUpScreenPreview",
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"
)

@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onValidLogin = {})
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onValidLogin: (String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Blue)
            .padding(
                start = dimensionResource(id = R.dimen.common_login_padding_start),
                end = dimensionResource(id = R.dimen.common_login_padding_end),
                bottom = dimensionResource(id = R.dimen.button_margin_bottom)
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
                color = White
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_margin_top_small)))
            Text(
                text = stringResource(id = R.string.enter_email_password),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = White
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
                    if (valid) onValidLogin(email)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.checkbox_frame_size))
                            .border(
                                width = dimensionResource(id = R.dimen.checkbox_border_width),
                                color = White,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.checkbox_corner_radius))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Transparent,
                                uncheckedColor = Color.Transparent,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.size(dimensionResource(id = R.dimen.checkbox_inner_size))
                        )
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_spacing)))
                    Text(
                        text = stringResource(R.string.remember_me),
                        color = GrayText2,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = stringResource(R.string.forgot_password),
                    color = White,
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
                    if (valid) onValidLogin(email)
                },
                borderColor = Orange,
                contentColor = White
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_spacing)))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.login_margin_bottom_text))
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_account),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText2
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_spacing)))
                Text(
                    text = stringResource(id = R.string.sign_up),
                    style = MaterialTheme.typography.bodySmall,
                    color = White,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, SignUpActivity::class.java))
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
