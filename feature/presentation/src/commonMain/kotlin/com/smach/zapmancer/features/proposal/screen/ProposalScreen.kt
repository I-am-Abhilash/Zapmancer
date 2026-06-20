package com.smach.zapmancer.features.proposal.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.proposal.state.ProposalStep
import com.smach.zapmancer.features.proposal.state.ProposalUiState

// Flip7 Palette
private val ZapTeal = Color(0xFF2BA8A2)
private val ZapGold = Color(0xFFD4AF37)
private val ZapBg = Color(0xFFEFF8F7)
private val ZapSurface = Color(0xFFFFFFFF)
private val ZapOnSurface = Color(0xFF191C1B)
private val ZapOnSurfaceVariant = Color(0xFF3F4948)
private val ZapOutline = Color(0xFF6F7978)
private val ZapOutlineVariant = Color(0xFFBEC9C7)
private val ZapSkyBlue = Color(0xFFEBF5FB)
private val ZapSkyBlueText = Color(0xFF2E86C1)
private val ZapCream = Color(0xFFFFF8E7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalScreen(
    onBackClick: () -> Unit = {}
) {
    var state by remember { mutableStateOf(ProposalUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Zapmancer",
                            color = ZapTeal,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ZapTeal
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ZapOnSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ZapTeal.copy(alpha = 0.1f))
                            .border(1.dp, ZapOutlineVariant, CircleShape)
                    ) {
                        // Avatar placeholder
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.8f))
            )
        },
        containerColor = ZapBg
    ) { padding ->
        ProposalContent(
            state = state,
            onStateChange = { state = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun ProposalContent(
    state: ProposalUiState,
    onStateChange: (ProposalUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepIndicator(currentStep = state.currentStep)

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedContent(targetState = state.currentStep) { step ->
            when (step) {
                1 -> DetailsStep(state, onNext = { onStateChange(state.copy(currentStep = 2)) })
                2 -> PitchStep(
                    state = state,
                    onPitchChange = { onStateChange(state.copy(pitchContent = it)) },
                    onBudgetChange = { onStateChange(state.copy(budget = it)) },
                    onTimelineChange = { onStateChange(state.copy(timelineDays = it)) },
                    onNext = { onStateChange(state.copy(currentStep = 3)) },
                    onBack = { onStateChange(state.copy(currentStep = 1)) }
                )

                3 -> ReviewStep(
                    state = state,
                    onNext = { onStateChange(state.copy(currentStep = 4)) },
                    onBack = { onStateChange(state.copy(currentStep = 2)) }
                )

                4 -> FinalizeStep(
                    state = state,
                    onBack = { onStateChange(state.copy(currentStep = 3)) },
                    onSubmit = { onStateChange(state.copy(isSubmitted = true)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

//        if (state.currentStep < 4) {
//            ContextInfoGrid()
//        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = ProposalStep.entries
    Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEach { step ->
                val isCompleted = currentStep > step.step
                val isCurrent = currentStep == step.step

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> ZapTeal
                                    isCurrent -> ZapGold
                                    else -> Color.White
                                }
                            )
                            .border(
                                2.dp,
                                if (isCompleted || isCurrent) Color.Transparent else ZapOutlineVariant,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = step.step.toString(),
                                color = if (isCurrent) Color.White else ZapOnSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Text(
                        text = step.title,
                        color = if (isCurrent) ZapGold else if (isCompleted) ZapTeal else ZapOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsStep(state: ProposalUiState, onNext: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        border = BorderStroke(1.dp, ZapOutlineVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Verify Your Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Text(
                "These details are automatically applied from your profile to ensure credibility.",
                style = MaterialTheme.typography.bodyMedium,
                color = ZapOnSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(ZapTeal.copy(alpha = 0.1f))
                        .border(1.dp, ZapTeal.copy(alpha = 0.3f), CircleShape)
                ) {
                    // Avatar placeholder
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        state.freelancerName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = ZapOnSurface
                    )
                    Text(
                        state.freelancerRole,
                        fontSize = 14.sp,
                        color = ZapTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                shape = CircleShape
            ) {
                Text("Confirm & Continue", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Composable
fun PitchStep(
    state: ProposalUiState,
    onPitchChange: (String) -> Unit,
    onBudgetChange: (String) -> Unit,
    onTimelineChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        border = BorderStroke(1.dp, ZapOutlineVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Your Pitch",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Text(
                "Clearly articulate how your skills align with the project requirements.",
                style = MaterialTheme.typography.bodyMedium,
                color = ZapOnSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Pro Tip
            Surface(
                color = ZapSkyBlue,
                border = BorderStroke(
                    1.dp,
                    ZapSkyBlueText.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = ZapSkyBlueText)
                    Column {
                        Text(
                            "PRO TIP",
                            fontWeight = FontWeight.Bold,
                            color = ZapSkyBlueText,
                            fontSize = 12.sp
                        )
                        Text(
                            "Focus on the client's problem, not just your services.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ZapSkyBlueText.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Pitch Content", fontWeight = FontWeight.Bold, color = ZapOnSurface)
            OutlinedTextField(
                value = state.pitchContent,
                onValueChange = onPitchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp),
                placeholder = {
                    Text(
                        "Write your compelling proposal here...",
                        color = ZapOutline
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ZapTeal,
                    unfocusedBorderColor = ZapOutlineVariant,
                    focusedContainerColor = ZapBg,
                    unfocusedContainerColor = ZapBg
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Text(
                "${state.pitchContent.length} / 2000",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = if (state.pitchContent.length > 2000) Color.Red else ZapOnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Proposed Budget", fontWeight = FontWeight.Bold, color = ZapOnSurface)
                    OutlinedTextField(
                        value = state.budget,
                        onValueChange = onBudgetChange,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        prefix = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZapTeal,
                            unfocusedBorderColor = ZapOutlineVariant,
                            focusedContainerColor = ZapBg,
                            unfocusedContainerColor = ZapBg
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Timeline (Days)", fontWeight = FontWeight.Bold, color = ZapOnSurface)
                    OutlinedTextField(
                        value = state.timelineDays,
                        onValueChange = onTimelineChange,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZapTeal,
                            unfocusedBorderColor = ZapOutlineVariant,
                            focusedContainerColor = ZapBg,
                            unfocusedContainerColor = ZapBg
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("Back", color = ZapOnSurfaceVariant, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                    shape = CircleShape,
                    enabled = state.pitchContent.isNotEmpty() && state.budget.isNotEmpty() && state.timelineDays.isNotEmpty()
                ) {
                    Text("Continue", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun ReviewStep(state: ProposalUiState, onNext: () -> Unit, onBack: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        border = BorderStroke(1.dp, ZapOutlineVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Review Proposal",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Text(
                "Please double-check your content before finalizing.",
                style = MaterialTheme.typography.bodyMedium,
                color = ZapOnSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(ZapTeal.copy(alpha = 0.1f))
                        .border(1.dp, ZapTeal.copy(alpha = 0.3f), CircleShape)
                ) {
                    // Avatar placeholder
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        state.freelancerName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = ZapOnSurface
                    )
                    Text(
                        state.freelancerRole,
                        fontSize = 14.sp,
                        color = ZapTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            BudgetSection(state)

            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                color = ZapSurface,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    state.pitchContent,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ZapOnSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("Back", color = ZapOnSurfaceVariant, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                    shape = CircleShape
                ) {
                    Text("Ready to Finalize", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun FinalizeStep(state: ProposalUiState, onBack: () -> Unit, onSubmit: () -> Unit) {
    if (state.isSubmitted) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ZapTeal,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Proposal Submitted!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Text(
                "Good luck! The client will review your pitch shortly.",
                textAlign = TextAlign.Center,
                color = ZapOnSurfaceVariant,
                modifier = Modifier.padding(horizontal = 32.dp).padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* Navigate Home */ },
                colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                shape = CircleShape
            ) {
                Text("Back to Dashboard", fontWeight = FontWeight.Bold)
            }
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ZapSurface),
            border = BorderStroke(1.dp, ZapOutlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Finalize Submission",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface
                )
                Text(
                    "By clicking submit, you agree to our Terms of Service and escrow guidelines.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ZapOnSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Icon(
                    Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = ZapTeal.copy(alpha = 0.7f),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                    shape = CircleShape
                ) {
                    Text("Submit Proposal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Back to Review", color = ZapOnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun BudgetSection(state: ProposalUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(12.dp),
//        border = BorderStroke(1.dp, ZapOutlineVariant.copy(alpha = 0.5f))
    ) {

        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Column (Modifier.padding(16.dp).weight(1f),
            ) {
                Text(
                    "BUDGET",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )
                Text(
                    state.budget,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = ZapOnSurface
                )
                Text(
                    state.projectType,
                    style = MaterialTheme.typography.bodySmall,
                    color = ZapOnSurface.copy(alpha = 0.9f)
                )
            }
//            VerticalDivider(
//                color = ZapOutlineVariant,
//                thickness = 1.dp
//            )
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                Text(
                    "TIMELINE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )
                Text(
                    state.timelineDays,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface
                )
                Text(
                    "Est. Start: ${state.estStart}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ZapOnSurface.copy(alpha = 0.9f)
                )
            }
        }
    }
}


//@Composable
//fun ContextInfoGrid() {
//    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
//        InfoCard(
//            Icons.Default.Lock,
//            "Secure Payment",
//            "Payments are held in escrow.",
//            Modifier.weight(1f)
//        )
//        InfoCard(
//            Icons.Default.History,
//            "Fast Review",
//            "Most clients review in 24-48h.",
//            Modifier.weight(1f)
//        )
//        InfoCard(
//            Icons.Default.SupportAgent,
//            "Support",
//            "24/7 success team guide.",
//            Modifier.weight(1f)
//        )
//    }
//}

//@Composable
//fun InfoCard(icon: ImageVector, title: String, description: String, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = ZapSurface),
//        border = BorderStroke(1.dp, ZapOutlineVariant),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Icon(icon, contentDescription = null, tint = ZapTeal, modifier = Modifier.size(24.dp))
//            Text(
//                title,
//                fontWeight = FontWeight.Bold,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            Text(description, fontSize = 11.sp, color = ZapOnSurfaceVariant, lineHeight = 14.sp)
//        }
//    }
//}

@Preview
@Composable
fun ProposalScreenPreview() {
    MaterialTheme {
        ProposalScreen()
    }
}
