package com.smach.zapmancer.features.projects.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.smach.zapmancer.features.common.adaptive.AdaptiveCenterContainer
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.projects.state.PostProjectUiState
import com.smach.zapmancer.features.projects.viewmodel.PostProjectEffect
import com.smach.zapmancer.features.projects.viewmodel.PostProjectEvent
import com.smach.zapmancer.features.projects.viewmodel.PostProjectViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PostProjectScreen(
    viewModel: PostProjectViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PostProjectEffect.ShowToast -> showSnackbar(effect.message)
                PostProjectEffect.NavigateBack -> onBackClick()
            }
        }
    }

    PostProjectContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun sharedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedContainerColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostProjectContent(
    state: PostProjectUiState,
    onEvent: (PostProjectEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                showBackButton = true,
                onBackClick = { onEvent(PostProjectEvent.BackClicked) },
                containerColor = MaterialTheme.colorScheme.surface,
                drawBottomBorder = true,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->


        AdaptiveCenterContainer(
            maxWidth = 840.dp,
            alignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter,
            ) {
                if (state.isSubmitted) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(80.dp),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Project Posted!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "Your project has been successfully listed. Freelancers can now view details and submit proposals.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = { onEvent(PostProjectEvent.BackClicked) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = CircleShape,
                        ) {
                            Text("Return to Dashboard", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        item {
                            Column {
                                Text(
                                    "Post a Project",
                                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    "Create a listing to find talented freelancers for your needs.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                        }

                        item { JobDetailsCard(state, onEvent) }
                        item { DeliverablesCard(state, onEvent) }
                        item { SkillsCard(state, onEvent) }

                        item {
                            Button(
                                onClick = { onEvent(PostProjectEvent.Submit) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White,
                                ),
                                shape = MaterialTheme.shapes.extraLarge,
                                enabled = !state.isSubmitting && state.title.isNotEmpty() && state.description.isNotEmpty(),
                            ) {
                                Text(
                                    if (state.isSubmitting) "Posting..." else "Post Project",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JobDetailsCard(state: PostProjectUiState, onEvent: (PostProjectEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Job Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Column {
                Text(
                    "Project Title",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { onEvent(PostProjectEvent.OnTitleChanged(it)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    placeholder = { Text("e.g. Kotlin Multiplatform Dev Needed") },
                    colors = sharedTextFieldColors(),
                    shape = MaterialTheme.shapes.small,
                )
            }
            Column {
                Text(
                    "Description / Scope",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { onEvent(PostProjectEvent.OnDescriptionChanged(it)) },
                    modifier = Modifier.fillMaxWidth().height(140.dp).padding(top = 8.dp),
                    placeholder = { Text("Provide details about deliverables, goals, and technical requirements...") },
                    colors = sharedTextFieldColors(),
                    shape = MaterialTheme.shapes.small,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Budget Range",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    OutlinedTextField(
                        value = state.budgetRange,
                        onValueChange = { onEvent(PostProjectEvent.OnBudgetChanged(it)) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        placeholder = { Text("e.g. $5K - $10K") },
                        colors = sharedTextFieldColors(),
                        shape = MaterialTheme.shapes.small,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Timeline",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    OutlinedTextField(
                        value = state.timeline,
                        onValueChange = { onEvent(PostProjectEvent.OnTimelineChanged(it)) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        placeholder = { Text("e.g. 2 Months") },
                        colors = sharedTextFieldColors(),
                        shape = MaterialTheme.shapes.small,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeliverablesCard(state: PostProjectUiState, onEvent: (PostProjectEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Deliverables",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = state.currentDeliverableInput,
                    onValueChange = { onEvent(PostProjectEvent.OnDeliverableInputChanged(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Add deliverable item...") },
                    colors = sharedTextFieldColors(),
                    shape = MaterialTheme.shapes.small,
                )
                FilledIconButton(
                    onClick = { onEvent(PostProjectEvent.AddDeliverable) },
                    modifier = Modifier.size(48.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.deliverables.forEachIndexed { index, deliverable ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.shapes.small,
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = deliverable,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        IconButton(
                            onClick = { onEvent(PostProjectEvent.RemoveDeliverable(index)) },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsCard(state: PostProjectUiState, onEvent: (PostProjectEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Required Skills",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = state.currentSkillInput,
                    onValueChange = { onEvent(PostProjectEvent.OnSkillInputChanged(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("e.g. Kotlin") },
                    colors = sharedTextFieldColors(),
                    shape = MaterialTheme.shapes.small,
                )
                FilledIconButton(
                    onClick = { onEvent(PostProjectEvent.AddSkill) },
                    modifier = Modifier.size(48.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.skills.forEachIndexed { index, skill ->
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.extraLarge,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = skill.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            IconButton(
                                onClick = { onEvent(PostProjectEvent.RemoveSkill(index)) },
                                modifier = Modifier.size(24.dp),
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PostProjectScreenPreview() {
    AppTheme {
        PostProjectContent(
            state = PostProjectUiState(),
            onEvent = {},
        )
    }
}
