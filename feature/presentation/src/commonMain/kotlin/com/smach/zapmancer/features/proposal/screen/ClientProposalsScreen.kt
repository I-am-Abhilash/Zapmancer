package com.smach.zapmancer.features.proposal.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.proposal.state.ClientProposalsUiState
import com.smach.zapmancer.features.proposal.viewmodel.ClientProposalsEffect
import com.smach.zapmancer.features.proposal.viewmodel.ClientProposalsEvent
import com.smach.zapmancer.features.proposal.viewmodel.ClientProposalsViewModel
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

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClientProposalsEffect.ShowToast -> {
                    showSnackbar(effect.message)
                }
            }
        }
    }

    ClientProposalsContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onFreelancerClick = onFreelancerClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProposalsContent(
    state: ClientProposalsUiState,
    onEvent: (ClientProposalsEvent) -> Unit,
    onBackClick: () -> Unit,
    onFreelancerClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                showBackButton = true,
                onBackClick = onBackClick,
                containerColor = MaterialTheme.colorScheme.surface,
                drawBottomBorder = true,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
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
            }

            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Loading proposals...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else if (state.proposals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No proposals received yet.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            } else {
                items(state.proposals) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onFreelancerClick = {
                            val id =
                                if (proposal.freelancerName.contains("Julian")) "julian_vancore" else "sarah_connor"
                            onFreelancerClick(id)
                        },
                        onAccept = { onEvent(ClientProposalsEvent.AcceptBid(proposal.freelancerName)) },
                        onMessage = { onEvent(ClientProposalsEvent.MessageFreelancer(proposal.freelancerName)) },
                    )
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
        shape = RoundedCornerShape(16.dp),
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
                shape = RoundedCornerShape(8.dp),
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
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Accept Bid", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Button(
                    onClick = onMessage,
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(8.dp),
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
    ClientProposalsContent(
        state = ClientProposalsUiState(),
        onEvent = {},
        onBackClick = {},
        onFreelancerClick = {},
    )
}
