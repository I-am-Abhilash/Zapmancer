package com.smach.zapmancer.presentation.proposal.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import com.smach.zapmancer.presentation.common.adaptive.isWideScreen
import com.smach.zapmancer.presentation.common.components.UserAvatar
import com.smach.zapmancer.presentation.common.components.ZapmancerTopBar
import com.smach.zapmancer.presentation.proposal.state.ClientProposalsUiState
import com.smach.zapmancer.presentation.proposal.viewmodel.ClientProposalsEffect
import com.smach.zapmancer.presentation.proposal.viewmodel.ClientProposalsEvent
import com.smach.zapmancer.presentation.proposal.viewmodel.ClientProposalsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClientProposalsScreen(
    projectId: String,
    viewModel: ClientProposalsViewModel = koinViewModel(parameters = {
        org.koin.core.parameter.parametersOf(
            projectId,
        )
    }),
    onBackClick: () -> Unit = {},
    onFreelancerClick: (String) -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClientProposalsEffect.ShowToast -> showSnackbar(effect.message)
                ClientProposalsEffect.NavigateBack -> onBackClick()
                is ClientProposalsEffect.NavigateToFreelancerProfile -> onFreelancerClick(effect.freelancerId)
            }
        }
    }

    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                showBackButton = true,
                onBackClick = { viewModel.onEvent(ClientProposalsEvent.BackClicked) },
                containerColor = MaterialTheme.colorScheme.surface,
                drawBottomBorder = true,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        ClientProposalsBody(
            paddingValues = padding,
            state = state,
            onEvent = viewModel::onEvent,
            windowSizeClass = windowSizeClass,
        )
    }
}

@Composable
fun ClientProposalsBody(
    paddingValues: PaddingValues,
    state: ClientProposalsUiState,
    onEvent: (ClientProposalsEvent) -> Unit,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (windowSizeClass.isWideScreen) 24.dp else 12.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                Text(
                    "Project Proposals",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Review pitches and bids received from qualified freelancers.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center,
                ) { Text("Loading proposals...", style = MaterialTheme.typography.bodyMedium) }
            } else if (state.proposals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No proposals received yet.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else if (windowSizeClass.isWideScreen) {
                // Wide Screen Grid (2 columns)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.proposals) { proposal ->
                        ProposalCard(
                            proposal = proposal,
                            onFreelancerClick = {
                                val id =
                                    if (proposal.freelancerName.contains("Julian")) "julian_vancore" else "sarah_connor"
                                onEvent(ClientProposalsEvent.FreelancerClicked(id))
                            },
                            onAccept = { onEvent(ClientProposalsEvent.AcceptBid(proposal.freelancerName)) },
                            onMessage = { onEvent(ClientProposalsEvent.MessageFreelancer(proposal.freelancerName)) },
                        )
                    }
                }
            } else {
                // Compact Screen List (1 column)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.proposals) { proposal ->
                        ProposalCard(
                            proposal = proposal,
                            onFreelancerClick = {
                                val id =
                                    if (proposal.freelancerName.contains("Julian")) "julian_vancore" else "sarah_connor"
                                onEvent(ClientProposalsEvent.FreelancerClicked(id))
                            },
                            onAccept = { onEvent(ClientProposalsEvent.AcceptBid(proposal.freelancerName)) },
                            onMessage = { onEvent(ClientProposalsEvent.MessageFreelancer(proposal.freelancerName)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProposalCard(
    proposal: com.smach.zapmancer.domain.model.Proposal,
    onFreelancerClick: () -> Unit,
    onAccept: () -> Unit,
    onMessage: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(
                    modifier = Modifier.weight(1f).clickable { onFreelancerClick() },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val avatarUrl = if (proposal.freelancerName.contains("Julian")) {
                        "https://lh3.googleusercontent.com/aida-public/AB6AXuBrBrKqoM8axW5MPKsBTP5b-rY47j3sPFMPKxLb9MC-OiKc2nVehBkyjSvjrG61iLhnECENazpIX7ZGYdSvJhKpIGWBgn-fNWKLOFOAoJvAOS7uUgeFV7IEUSxjbQHtWEbwQGrVnBP5GX0LOssfjYZWHQOHZeoQNPaT0aZZAB44DcV0MaETyz8F_dFWst5O4bhj6tODWrstc0H0BKuGeulwq4Nbqlg5_5SCdjeZWbq0lUi7AAm8ZezuoaO1rWJpKniR5CNjmrAo9eo"
                    } else {
                        "https://lh3.googleusercontent.com/aida-public/AB6AXuAX8tbMna09O86Wf5o2nHWHxsqjy7PARWhxZXBCVEWOe7KfAJ9eTK1JqA5LH7wh8NgBt2Dh6YT7t34Ay3Wm__NSI__FFShGQSbJT4vkBQFPnYTgUToF5QZpYgZESL4TgKbxoPgnYfjj4GMYJzn4J3FI3CapiStdQ4GlZKecwDNJTuDGIfCHXG_De4Gzw8Fr-oziYeoZIy01oCMOTAKtIivyNuH68QFqBjeLpkJAea8JDdWbxSePLlbr5U5_jhrpqzToIO5g-Oz5yfs"
                    }
                    UserAvatar(
                        imageUrl = avatarUrl,
                        size = 48.dp,
                        borderWidth = 1.5.dp,
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = proposal.freelancerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = proposal.freelancerRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${proposal.budget}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "${proposal.timelineDays} days",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "\"${proposal.pitchContent}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 20.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.small,
                ) { Text("Accept Bid", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                Button(
                    onClick = onMessage,
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Icon(
                        Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Message", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun ClientProposalScreenPreview() {
    ClientProposalsBody(
        paddingValues = PaddingValues(0.dp),
        state = ClientProposalsUiState(),
        onEvent = {},
    )
}
