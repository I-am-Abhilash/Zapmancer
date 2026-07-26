package com.smach.zapmancer.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.smach.zapmancer.domain.model.ProjectCategory

fun ProjectCategory.icon(): ImageVector = when (this) {
    ProjectCategory.DESIGN -> Icons.Outlined.Palette
    ProjectCategory.DEVELOPMENT -> Icons.Outlined.Devices
    ProjectCategory.MARKETING -> Icons.Outlined.Campaign
    ProjectCategory.ALL -> Icons.Outlined.Devices
    ProjectCategory.WRITING -> Icons.Outlined.Translate
    ProjectCategory.MULTIMEDIA -> Icons.Outlined.VideoLibrary
    ProjectCategory.CONSULTING -> Icons.Outlined.BusinessCenter
    ProjectCategory.ADMIN -> Icons.Outlined.Assignment
    ProjectCategory.FINANCE -> Icons.Outlined.AccountBalance
    ProjectCategory.LEGAL -> Icons.Outlined.Gavel
    ProjectCategory.ANALYTICS -> Icons.Outlined.Analytics
    ProjectCategory.SECURITY -> Icons.Outlined.Security
    ProjectCategory.CUSTOMER_SUPPORT -> Icons.Outlined.SupportAgent
}

fun ProjectCategory.accentColor(colors: ColorScheme): Color = when (this) {
    ProjectCategory.DESIGN -> colors.tertiary
    ProjectCategory.DEVELOPMENT -> colors.primary
    ProjectCategory.MARKETING -> colors.secondary
    ProjectCategory.ALL -> colors.primary
    ProjectCategory.WRITING -> colors.secondary
    ProjectCategory.MULTIMEDIA -> colors.tertiary
    ProjectCategory.CONSULTING -> colors.primary
    ProjectCategory.ADMIN -> colors.outline
    ProjectCategory.FINANCE -> colors.primary
    ProjectCategory.LEGAL -> colors.tertiary
    ProjectCategory.ANALYTICS -> colors.secondary
    ProjectCategory.SECURITY -> colors.primary
    ProjectCategory.CUSTOMER_SUPPORT -> colors.outline
}
