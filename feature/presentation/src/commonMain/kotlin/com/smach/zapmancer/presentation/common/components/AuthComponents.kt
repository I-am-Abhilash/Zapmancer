package com.smach.zapmancer.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.presentation.common.theme.input
import com.smach.zapmancer.presentation.common.theme.pill

// ─── Auth Layout ──────────────────────────────────────────────────────────────
// Fully design-agnostic: all colors come from MaterialTheme.colorScheme,
// all shapes from MaterialTheme.shapes semantic aliases.
//
//   Left brand panel  → colorScheme.primary fill, onPrimary text
//   Form panel        → colorScheme.background / surface
//   Icon well         → colorScheme.primary fill, onPrimary icon (shapes.extraSmall = square)
//   Text field        → surfaceVariant container, primary focus border (shapes.input)
//   Social button     → outline variant border, surface fill (shapes.pill)
//   Divider           → outline color
//
// @Composable
// fun AuthAdaptiveLayout(
//    formContent: @Composable () -> Unit,
// ) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//            .padding(16.dp),
//    ) {
//        AuthBrandHeader()
//        Spacer(modifier = Modifier.height(24.dp))
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.TopCenter,
//        ) {
//            formContent()
//        }
//    }
//
// //    when (windowLayout) {
// //        WindowLayout.Compact -> {
// //            Column(
// //                modifier = Modifier
// //                    .fillMaxSize()
// //                    .background(MaterialTheme.colorScheme.background)
// //                    .padding(16.dp),
// //            ) {
// //                AuthBrandHeader()
// //                Spacer(modifier = Modifier.height(24.dp))
// //                Box(
// //                    modifier = Modifier.fillMaxSize(),
// //                    contentAlignment = Alignment.TopCenter,
// //                ) {
// //                    formContent()
// //                }
// //            }
// //        }
// //
// //        WindowLayout.Medium -> {
// //            Row(modifier = Modifier.fillMaxSize()) {
// //                // Left panel: primary fill (NikeInk in Nike theme)
// //                Box(
// //                    modifier = Modifier
// //                        .weight(0.45f)
// //                        .fillMaxHeight()
// //                        .background(MaterialTheme.colorScheme.primary),
// //                    contentAlignment = Alignment.Center,
// //                ) {
// //                    AuthBrandPanel(iconSize = 96.dp, titleSize = 40.sp)
// //                }
// //                Box(
// //                    modifier = Modifier
// //                        .weight(0.55f)
// //                        .fillMaxHeight()
// //                        .background(MaterialTheme.colorScheme.background),
// //                    contentAlignment = Alignment.Center,
// //                ) {
// //                    Box(
// //                        modifier = Modifier
// //                            .widthIn(max = 480.dp)
// //                            .padding(32.dp),
// //                    ) {
// //                        formContent()
// //                    }
// //                }
// //            }
// //        }
// //
// //        WindowLayout.Expanded -> {
// //            Row(modifier = Modifier.fillMaxSize()) {
// //                Box(
// //                    modifier = Modifier
// //                        .weight(0.55f)
// //                        .fillMaxHeight()
// //                        .background(MaterialTheme.colorScheme.primary),
// //                    contentAlignment = Alignment.Center,
// //                ) {
// //                    AuthBrandPanel(iconSize = 140.dp, titleSize = 56.sp)
// //                }
// //                Box(
// //                    modifier = Modifier
// //                        .weight(0.45f)
// //                        .fillMaxHeight()
// //                        .background(MaterialTheme.colorScheme.background),
// //                    contentAlignment = Alignment.Center,
// //                ) {
// //                    Box(
// //                        modifier = Modifier
// //                            .widthIn(max = 520.dp)
// //                            .padding(48.dp),
// //                    ) {
// //                        formContent()
// //                    }
// //                }
// //            }
// //        }
// //    }
// }

// Compact header: brand mark on background canvas
@Composable
private fun AuthBrandHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Default.Bolt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Zapmancer",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 0.sp,
        )
    }
}

// Wide-screen brand panel: onPrimary text on primary background
@Composable
private fun AuthBrandPanel(
    iconSize: androidx.compose.ui.unit.Dp,
    titleSize: androidx.compose.ui.unit.TextUnit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.Bolt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "ZAPMANCER",
            style = MaterialTheme.typography.displayMedium.copy(fontSize = titleSize),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = 0.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Build the future of decentralized apps.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
    }
}

// ─── Auth Header ──────────────────────────────────────────────────────────────
// Icon well uses extraSmall (0dp = square in Nike, rounded in other themes)

@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.Bolt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp),
            )
        }
        Text(
            text = "ZAPMANCER",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 0.sp,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Text Field ───────────────────────────────────────────────────────────────
// Shape: shapes.input (medium slot = 24dp in Nike, swappable via Shape.kt)
// Colors: surfaceVariant container, primary focus border, outline unfocused border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZapTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = true,
    onTogglePassword: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    error: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onTogglePassword ?: {}) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                null
            },
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
            ),
            shape = MaterialTheme.shapes.input, // medium = 24dp in Nike
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = error != null,
            singleLine = true,
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

// ─── Social Auth Button ───────────────────────────────────────────────────────
// Shape: shapes.pill (extraLarge = 9999dp in Nike, swappable)
// Colors: surface fill, outline border, onSurface text

@Composable
fun SocialAuthButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.pill,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ─── Auth Divider ─────────────────────────────────────────────────────────────

@Composable
fun AuthDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = "OR",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
