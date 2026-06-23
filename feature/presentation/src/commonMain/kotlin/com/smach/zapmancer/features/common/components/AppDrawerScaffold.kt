package com.smach.zapmancer.features.common.components

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun AppDrawerScaffold(
    isClientMode: Boolean,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onNavigateToProposal: () -> Unit,
    content: @Composable () -> Unit,
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
    )

    val scope = rememberCoroutineScope()

    val drawerController = remember {
        DrawerController(
            drawerState = drawerState,
            scope = scope,
        )
    }

    CompositionLocalProvider(
        LocalDrawerController provides drawerController,
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    isClientMode = isClientMode,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToSettings = onNavigateToSettings,
                    onCreateProjectClick = onCreateProjectClick,
                    onNavigateToProposal = onNavigateToProposal,
                    closeDrawer = drawerController::close,
                )
            },
        ) {
            content()
        }
    }
}
