package com.spp.android.myapplication.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.spp.android.myapplication.FilledButton
import com.spp.android.myapplication.OutlinedBorderButton
import com.spp.android.myapplication.R
import com.spp.android.myapplication.data.UserPreferences
import com.spp.android.myapplication.ui.components.MaterialStyledTextField
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.Blue
import com.spp.android.myapplication.ui.theme.GrayText2
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import com.spp.android.myapplication.ui.theme.Orange
import com.spp.android.myapplication.ui.theme.White
import kotlinx.coroutines.launch

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                SignUpScreen(onSuccessfulSignUp = { email ->
                    lifecycleScope.launch {
                        UserPreferences.saveEmail(this@SignUpActivity, email)

                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java).apply {
                            putExtra("email", email)
                        })
                        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                        finish()
                    }
                })
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "SignUpScreenPreview",
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"
)

@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen(onSuccessfulSignUp = {})
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSuccessfulSignUp: (String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Blue)
            .padding(
                start = dimensionResource(id = R.dimen.common_login_padding_start),
                end = dimensionResource(id = R.dimen.common_login_padding_end),
                bottom = 24.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_top_large)))
            Text(
                text = stringResource(R.string.signup_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = White
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
            Text(
                text = stringResource(R.string.signup_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = GrayText2
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_login_margin_top_medium)))
            Text(
                text = stringResource(R.string.email),
                style = MaterialTheme.typography.bodySmall,
                color = GrayText2
            )
            MaterialStyledTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                errorMessage = emailError
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodySmall,
                color = GrayText2
            )
            MaterialStyledTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                isPassword = true,
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
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
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText2
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledButton(
                text = stringResource(R.string.signup_google).uppercase(),
                onClick = { /* TODO: Google sign-up */ },
                containerColor = White,
                contentColor = GrayText2
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
            Text(
                text = stringResource(R.string.signup_or),
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
            OutlinedBorderButton(
                text = stringResource(R.string.signup_register).uppercase(),
                onClick = {
                    val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val passwordValid =
                        password.length >= context.resources.getInteger(R.integer.password_length)
                    var isValid = true
                    if (!emailValid) {
                        emailError = context.getString(R.string.email_error_text)
                        isValid = false
                    }
                    if (!passwordValid) {
                        passwordError = context.getString(R.string.password_error_text)
                        isValid = false
                    }
                    if (!isValid) return@OutlinedBorderButton
                    onSuccessfulSignUp(email)
                },
                borderColor = Orange,
                contentColor = White
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacing)))
            Text(
                text = stringResource(R.string.signup_terms),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,

                color = GrayText2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.signup_have_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText2
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.signup_sign_in),
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as? Activity)?.finish()
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
