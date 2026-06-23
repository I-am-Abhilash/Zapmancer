package com.smach.zapmancer.features.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smach.zapmancer.features.auth.state.ForgotPasswordUiState
import com.smach.zapmancer.features.auth.viewmodel.ForgotPasswordEvent
import com.smach.zapmancer.features.auth.viewmodel.ForgotPasswordViewModel
import com.smach.zapmancer.features.common.components.AuthHeader
import com.smach.zapmancer.features.common.components.ZapTextField
import org.koin.compose.viewmodel.koinViewModel

// Flip7 Palette

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackToLogin: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ForgotPasswordContent(
        state = state,
        onEmailChange = { viewModel.onEvent(ForgotPasswordEvent.EmailChanged(it)) },
        onSubmit = { viewModel.onEvent(ForgotPasswordEvent.Submit) },
        onBackToLogin = onBackToLogin,
    )
}

@Composable
private fun ForgotPasswordContent(
    state: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            AuthHeader(
                title = "Forgot Password",
                subtitle = "Enter your email to receive a recovery link",
            )

            // Form Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(8.dp)),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    // Email Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Email Address",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        ZapTextField(
                            value = state.email,
                            onValueChange = onEmailChange,
                            placeholder = "name@company.com",
                            leadingIcon = Icons.Default.Mail,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                        )
                    }

                    if (state.error != null) {
                        Text(state.error, color = Color.Red, fontSize = 12.sp)
                    }

                    if (state.isSuccess) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Recovery email sent! Check your inbox.",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(2.dp, RoundedCornerShape(100.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface,
                        ),
                        shape = RoundedCornerShape(100.dp),
                        enabled = !state.isLoading && !state.isSuccess,
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
                                    "Send Recovery Link",
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
                }
            }

            // Footer
            Text(
                "Back to Login",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onBackToLogin() },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordContent(
            state = ForgotPasswordUiState(),
            onEmailChange = {},
            onSubmit = {},
            onBackToLogin = {},
        )
    }
}
