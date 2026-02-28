package com.spp.android.myapplication.presentation.notifications

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun EnsureNotificationsPermission() {
    if (android.os.Build.VERSION.SDK_INT < 33) return

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted ->
        android.util.Log.d("NotifPerm", "POST_NOTIFICATIONS granted=$granted")
    }

    LaunchedEffect(Unit) {
        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        android.util.Log.d("NotifPerm", "alreadyGranted=$granted")
        if (!granted) launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
}