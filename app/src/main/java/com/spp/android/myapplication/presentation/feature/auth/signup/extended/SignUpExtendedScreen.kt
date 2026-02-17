package com.spp.android.myapplication.presentation.feature.auth.signup.extended

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.BackFromExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.NavigateToHome
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.OpenAvatarPicker
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.AvatarPicked
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.CancelExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.ForwardExtended
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PhoneChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.PickAvatar
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpContract.Event.UsernameChanged
import com.spp.android.myapplication.presentation.feature.auth.signup.SignUpViewModel
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerScreen
import com.spp.android.myapplication.presentation.utils.parseNameFromEmail
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpExtendedScreen(
    onBack: () -> Unit,
    onNavigateHome: (String, String) -> Unit = { _, _ -> },
    prefillEmail: String? = null,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val snackBar = SnackbarHostState()
    var showPicker by remember { mutableStateOf(false) }
    var pickerKey by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    if (showPicker) {
        key(pickerKey) {
            GalleryPickerScreen(
                startVisible = true,
                onResult = { uri ->
                    showPicker = false
                    uri?.let { viewModel.onEvent(AvatarPicked(it.toString())) }
                },
            )
        }
    }

    LaunchedEffect(prefillEmail, profile.username) {
        if (!prefillEmail.isNullOrBlank() && profile.username.isBlank()) {
            val (first, last) = parseNameFromEmail(prefillEmail)
            val full = listOf(first, last).filter { it.isNotBlank() }.joinToString(" ")
            if (full.isNotBlank()) {
                viewModel.onEvent(UsernameChanged(full))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ShowMessage -> snackBar.showSnackbar(effect.message.text(context))
                OpenAvatarPicker -> {
                    pickerKey++
                    showPicker = true
                }

                is BackFromExtended -> onBack()
                is NavigateToHome -> {
                    val email = prefillEmail ?: viewModel.state.value.fields.email
                    val username = profile.username
                    onNavigateHome(email, username)
                }

                is SignUpContract.Effect.ShowToast -> {
                    Toast.makeText(context, effect.text, Toast.LENGTH_LONG).show()
                }

                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBar) }) { paddings ->
        SignUpProfileScreenContent(
            state =
                SignUpContract.ProfileState(
                    username = profile.username,
                    phone = profile.phone,
                    usernameErrorKey = profile.usernameErrorKey,
                    phoneErrorKey = profile.phoneErrorKey,
                    avatarPath = profile.avatarPath,
                ),
            onPickAvatar = { viewModel.onEvent(PickAvatar) },
            onUserNameChange = { viewModel.onEvent(UsernameChanged(it)) },
            onPhoneChange = { viewModel.onEvent(PhoneChanged(it)) },
            onCancel = { viewModel.onEvent(CancelExtended) },
            onForward = { viewModel.onEvent(ForwardExtended) },
            modifier = Modifier.padding(paddings),
        )
    }

    if (showPicker) {
        GalleryPickerScreen(
            startVisible = true,
            onResult = { uri ->
                showPicker = false
                uri?.let { viewModel.onEvent(AvatarPicked(it.toString())) }
            },
        )
    }
}
