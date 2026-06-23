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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smach.zapmancer.features.auth.state.LoginUiState
import com.smach.zapmancer.features.auth.viewmodel.LoginEvent
import com.smach.zapmancer.features.auth.viewmodel.LoginViewModel
import com.smach.zapmancer.features.common.components.AuthDivider
import com.smach.zapmancer.features.common.components.AuthHeader
import com.smach.zapmancer.features.common.components.SocialAuthButton
import com.smach.zapmancer.features.common.components.ZapTextField
import org.koin.compose.viewmodel.koinViewModel

// Flip7 Palette

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToForgot: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {},
    onLoginSuccess: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    LoginContent(
        state = state,
        onEmailChanged = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) },
        onPasswordChanged = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
        onSubmit = { viewModel.onEvent(LoginEvent.Submit) },
        onTogglePassword = { viewModel.onEvent(LoginEvent.OnTogglePasswordVisibility) },
        onNavigateToForgot = onNavigateToForgot,
        onNavigateToSignup = onNavigateToSignup,
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onTogglePassword: () -> Unit,
    onNavigateToForgot: () -> Unit,
    onNavigateToSignup: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
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
                title = "Welcome Back",
                subtitle = "Sign in to your dashboard",
            )

            // Login Form Card
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
                            onValueChange = onEmailChanged,
                            placeholder = "name@company.com",
                            leadingIcon = Icons.Default.Mail,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down,
                                )
                            }),
                        )
                    }

                    // Password Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                "Password",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                "Forgot password?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { onNavigateToForgot() },
                            )
                        }
                        ZapTextField(
                            value = state.password,
                            onValueChange = onPasswordChanged,
                            placeholder = "••••••••",
                            leadingIcon = Icons.Default.Lock,
                            isPassword = true,
                            isPasswordVisible = !state.togglePassword,
                            onTogglePassword = onTogglePassword,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                        )
                    }

                    if (state.error != null) {
                        Text(state.error, color = Color.Red, fontSize = 12.sp)
                    }

                    // Sign In Button
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
                        enabled = !state.isLoading,
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
                                Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }

                    AuthDivider()

                    SocialAuthButton(
                        onClick = {},
                        text = "Continue with Google",
                        icon = Icons.Default.Person,
                    )
                }
            }

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    "Don't have an account?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Join",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToSignup() },
                )
            }

            // System Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.alpha(0.6f),
            ) {
                Box(
                    modifier = Modifier.size(8.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
                Text(
                    "All systems operational",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginUiState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onSubmit = {},
            onTogglePassword = {},
            onNavigateToForgot = {},
            onNavigateToSignup = {},
        )
    }
}
