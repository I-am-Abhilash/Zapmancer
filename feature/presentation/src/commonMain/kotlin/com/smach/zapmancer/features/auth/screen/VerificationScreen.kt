package com.smach.zapmancer.features.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smach.zapmancer.features.auth.state.VerificationUiState
import com.smach.zapmancer.features.auth.viewmodel.VerificationEvent
import com.smach.zapmancer.features.auth.viewmodel.VerificationViewModel
import com.smach.zapmancer.features.common.adaptive.AdaptiveCenterContainer
import com.smach.zapmancer.features.common.components.AuthHeader
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VerificationScreen(
    email: String,
    onBack: () -> Unit,
    onVerificationSuccess: () -> Unit,
) {
    val viewModel = koinViewModel<VerificationViewModel>(
        key = "verification_$email",
        parameters = { parametersOf(email) },
    )

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
    val codeLength = 6

    AdaptiveCenterContainer {
        Column(
            modifier = Modifier.verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            AuthHeader(
                title = "Verify Email",
                subtitle = "We've sent a 6-digit code to $email",
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth().shadow(1.dp, MaterialTheme.shapes.small),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OtpInputField(
                        code = state.code,
                        onCodeChanged = onCodeChanged,
                        length = codeLength,
                        enabled = !state.isLoading,
                    )

                    if (state.error != null) {
                        Text(state.error, color = Color.Red, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface,
                        ),
                        shape = MaterialTheme.shapes.extraLarge,
                        enabled = !state.isLoading && state.code.length == codeLength,
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    "Verify & Continue",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                )
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }

                    TextButton(onClick = { /* Resend */ }) {
                        Text(
                            "Didn't receive code? Resend",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        )
                    }
                }
            }

            Text(
                "Change Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onBack() }.alpha(0.6f),
            )
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
    Box(contentAlignment = Alignment.Center) {
        OutlinedTextField(
            value = code,
            onValueChange = {
                if (it.length <= length && it.all { char -> char.isDigit() }) {
                    onCodeChanged(it)
                }
            },
            modifier = Modifier.size(0.dp),
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
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = if (isFocused && enabled) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
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
            state = VerificationUiState(),
            onCodeChanged = {},
            onSubmit = {},
            onBack = {},
        )
    }
}
