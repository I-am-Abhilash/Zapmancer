package com.smach.zapmancer.features.common.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Flip7 Palette for Auth Components
private val ZapTeal = Color(0xFF2BA8A2)
private val ZapOnSurfaceVariant = Color(0xFF404948)
private val ZapOutline = Color(0xFFD1DBDA)
private val ZapCream = Color(0xFFE2E2E2)

@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(ZapTeal, RoundedCornerShape(8.dp))
                .shadow(2.dp, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Bolt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Zapmancer",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1C1C),
            letterSpacing = (-0.5).sp
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = ZapOnSurfaceVariant,
        )
    }
}

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
            placeholder = { Text(placeholder, color = ZapOnSurfaceVariant.copy(alpha = 0.5f)) },
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = ZapOnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onTogglePassword ?: {}) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ZapCream,
                unfocusedContainerColor = ZapCream,
                focusedBorderColor = ZapTeal,
                unfocusedBorderColor = ZapOutline,
                focusedLeadingIconColor = ZapTeal
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = error != null,
            singleLine = true
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

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
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF1A1C1C),
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

@Composable
fun AuthDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = ZapOutline)
        Text(
            text = "OR",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = ZapOnSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = ZapOutline)
    }
}
