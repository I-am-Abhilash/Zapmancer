package com.smach.zapmancer.features.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smach.zapmancer.features.auth.state.VerificationUiState
import com.smach.zapmancer.features.auth.viewmodel.VerificationEvent
import com.smach.zapmancer.features.auth.viewmodel.VerificationViewModel
import com.smach.zapmancer.features.common.components.AuthHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerificationScreen(
    email: String,
    viewModel: VerificationViewModel = koinViewModel(),
    onBack: () -> Unit,
    onVerificationSuccess: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onVerificationSuccess()
        }
    }

    VerificationContent(
        email = email,
        state = state,
        onCodeChanged = { viewModel.onEvent(VerificationEvent.CodeChanged(it)) },
        onSubmit = { viewModel.onEvent(VerificationEvent.Submit) },
        onBack = onBack,
    )
}

@Composable
private fun VerificationContent(
    email: String,
    state: VerificationUiState,
    onCodeChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // We'll use a single string but display it in separate boxes for better UX
    // Assuming 6-digit code
    val codeLength = 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            AuthHeader(
                title = "Verify Email",
                subtitle = "We've sent a 6-digit code to $email",
            )

            Spacer(modifier = Modifier.height(48.dp))

            // OTP Input Area
            OtpInputField(
                code = state.code,
                onCodeChanged = onCodeChanged,
                length = codeLength,
                enabled = !state.isLoading,
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !state.isLoading && state.code.length == codeLength,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Verify & Continue")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { /* TODO: Resend logic */ }) {
                Text("Didn't receive code? Resend")
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onBack) {
                Text("Change Email")
            }
        }
    }
}

@Composable
fun OtpInputField(
    code: String,
    onCodeChanged: (String) -> Unit,
    length: Int,
    enabled: Boolean,
) {
    // Hidden TextField to capture actual input
    // This is easier than managing 6 individual focus states manually in KMP
    Box(contentAlignment = Alignment.Center) {
        OutlinedTextField(
            value = code,
            onValueChange = {
                if (it.length <= length && it.all { char -> char.isDigit() }) {
                    onCodeChanged(it)
                }
            },
            modifier = Modifier.size(0.dp), // Hide it
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            enabled = enabled,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(length) { index ->
                val char = code.getOrNull(index)?.toString() ?: ""
                val isFocused = code.length == index

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (enabled) 1f else 0.5f),
                    border = if (isFocused) {
                        androidx.compose.foundation.BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        )
                    },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        // Show a cursor-like indicator if focused
                        if (isFocused && enabled) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(MaterialTheme.colorScheme.primary),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationContentPreview() {
    MaterialTheme {
        VerificationContent(
            email = "test@example.com",
            state = VerificationUiState(
                code = "123456",
                isLoading = false,
                error = null,
            ),
            onCodeChanged = {},
            onSubmit = {},
            onBack = {},
        )
    }
}
