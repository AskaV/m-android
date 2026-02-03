package com.spp.android.myapplication.presentation.feature.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

@Stable
class UndoSnackBar(
    val hostState: SnackbarHostState,
    private val totalSeconds: Int,
) {
    var secondsLeft by mutableIntStateOf(0)
        private set

    fun showUndo(
        scope: CoroutineScope,
        message: String = "Item deleted",
        undoLabel: String = "Undo",
        onUndo: () -> Unit = {},
        onTimeout: () -> Unit = {},
    ) {
        secondsLeft = totalSeconds
        var timeoutJob: Job? = null

        scope.launch {
            val showJob =
                launch {
                    val result =
                        hostState.showSnackbar(
                            message = message,
                            actionLabel = undoLabel,
                            withDismissAction = false,
                            duration = SnackbarDuration.Indefinite,
                        )
                    if (result == SnackbarResult.ActionPerformed) {
                        timeoutJob?.cancel()
                        secondsLeft = 0
                        onUndo()
                    } else {
                        onTimeout()
                    }
                }

            timeoutJob =
                launch {
                    while (secondsLeft > 0) {
                        delay(1_000)
                        secondsLeft = max(0, secondsLeft - 1)
                    }
                    hostState.currentSnackbarData?.dismiss()
                }

            showJob.join()
            secondsLeft = 0
        }
    }

    @Composable
    fun Host() {
        SnackbarHost(hostState = hostState) { data ->
            if (secondsLeft > 0 && data.visuals.actionLabel != null) {
                UndoSnackbarWithTimer(
                    message = data.visuals.message,
                    secondsLeft = secondsLeft,
                    totalSeconds = totalSeconds,
                    onUndo = { data.performAction() },
                )
            } else {
                Snackbar(snackbarData = data)
            }
        }
    }
}

@Composable
fun rememberUndoSnackbarController(totalSeconds: Int = 5): UndoSnackBar {
    val host = remember { SnackbarHostState() }
    return remember(totalSeconds) { UndoSnackBar(host, totalSeconds) }
}

@Composable
private fun UndoSnackbarWithTimer(
    message: String,
    secondsLeft: Int,
    totalSeconds: Int,
    onUndo: () -> Unit,
) {
    Surface(
        tonalElevation = 6.dp,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onUndo) {
                    Text("Undo ($secondsLeft)")
                }
            }
            val progress = ((totalSeconds - secondsLeft).toFloat() / totalSeconds).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
        }
    }
}
