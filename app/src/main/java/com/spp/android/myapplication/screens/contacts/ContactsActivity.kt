package com.spp.android.myapplication.screens.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.spp.android.myapplication.R
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class ContactsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            .getString("theme_pref", "system") ?: "system"

        setContent {
            MyApplicationTheme(themePref = themePref) {
                ContactsScreen(onValidLogin = {})
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "ContactsScreenPreview",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"
)

@Composable
fun ContactsScreenPreview() {
    MyApplicationTheme(themePref = "system") { // colored or system
        ContactsScreen(onValidLogin = {})
    }
}

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    onValidLogin: (String) -> Unit,
    viewModel: ContactsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))

            ) {

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.TopStart)
                        .padding(top = dimensionResource(id = R.dimen.spacer_medium))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                    )
                }

                Text(
                    text = stringResource(R.string.contacts_text),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.TopCenter)
                        .padding(top = dimensionResource(id = R.dimen.spacer_medium))
                )

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(top = dimensionResource(id = R.dimen.spacer_medium))

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_button),
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                    )
                }

                Text(
                    text = stringResource(R.string.add_contacts_text),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = dimensionResource(id = R.dimen.spacer_large))
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacer_small))
                ) {
                    items(contacts.size) { index ->
                        val contact = contacts[index]
                        val message = stringResource(R.string.deleted_contact_toast_text)
                        val actionLabel = stringResource(R.string.return_contact_toast_text)
                        SwipeToDeleteContainer(
                            contact = contact,
                            onDelete = {
                                viewModel.deleteContact(contact)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message =message,
                                        actionLabel = actionLabel,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoDelete()
                                    }
                                }
                            }
                        ) {
                            ContactItem(contact = contact) {
                                viewModel.deleteContact(contact)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = message,
                                        actionLabel = actionLabel,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoDelete()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteContainer(
    contact: Contact,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isRemoved by remember { mutableStateOf(false) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300),
        label = "swipe-offset"
    )

    val maxSwipe = 300f
    val minSwipe = 0f

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Top
        )
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(minSwipe, maxSwipe)
                        },
                        onDragEnd = {
                            if (offsetX >= maxSwipe * 0.5f) {
                                isRemoved = true
                                onDelete()
                            } else {
                                offsetX = 0f
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}