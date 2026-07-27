package com.smach.zapmancer.presentation.landingpage.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.FeaturedProject
import com.smach.zapmancer.domain.model.FeaturedTalent
import com.smach.zapmancer.domain.model.HowItWorksStep
import com.smach.zapmancer.domain.model.LandingPageData
import com.smach.zapmancer.domain.model.MarketplaceCategory
import com.smach.zapmancer.domain.model.ProductCard
import com.smach.zapmancer.domain.model.ResourceCard
import com.smach.zapmancer.domain.model.SolutionCard
import com.smach.zapmancer.domain.model.SuccessStory
import com.smach.zapmancer.domain.model.TrustSafetyItem
import com.smach.zapmancer.presentation.common.components.AppShimmer
import com.smach.zapmancer.presentation.common.components.UserAvatar
import com.smach.zapmancer.presentation.common.theme.pill
import com.smach.zapmancer.presentation.landingpage.state.LandingPageUiState
import com.smach.zapmancer.presentation.landingpage.viewmodel.LandingPageEffect
import com.smach.zapmancer.presentation.landingpage.viewmodel.LandingPageEvent
import com.smach.zapmancer.presentation.landingpage.viewmodel.LandingPageViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LandingPageScreen(
    viewModel: LandingPageViewModel = koinViewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LandingPageEffect.ShowToast -> showSnackbar(effect.message)
                LandingPageEffect.NavigateToSearchTalent -> onNavigateToSearch()
                LandingPageEffect.NavigateToBrowseProjects -> onNavigateToProjects()
                LandingPageEffect.NavigateToLogin -> onNavigateToLogin()
                LandingPageEffect.NavigateToSignUp -> onNavigateToSignUp()
            }
        }
    }

    LandingPageContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun LandingPageContent(
    state: LandingPageUiState,
    onEvent: (LandingPageEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            GlobalNavigationHeader(
                activeMegaMenu = state.activeMegaMenu,
                onEvent = onEvent,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            val data = state.data
            if (state.isLoading || data == null) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    AppShimmer(modifier = Modifier.width(300.dp).height(40.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    AppShimmer(modifier = Modifier.width(500.dp).height(24.dp))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 0.dp),
                ) {
                    // 1. Hero Section
                    item { HeroSection(data.hero, onEvent) }

                    // 2. Two-Path "Whatever you're building"
                    item { TwoPathsSection(data.twoPaths, onEvent) }

                    // 3. Products Section
                    item { ProductsSection(data.products) }

                    // 4. Solutions Section
                    item { SolutionsSection(data.solutions) }

                    // 5. How Zapmancer Works
                    item { HowItWorksSection(data.howItWorksSteps) }

                    // 6. Marketplace Showcase & Skills
                    item { MarketplaceShowcaseSection(data.marketplaceCategories) }

                    // 7. Featured Talent & Projects
                    item { FeaturedSection(data.featuredTalent, data.featuredProjects) }

                    // 8. Trust & Safety Section
                    item { TrustSafetySection(data.trustItems) }

                    // 9. Success Stories
                    item { SuccessStoriesSection(data.successStories) }

                    // 10. Resources Section
                    item { ResourcesSection(data.resourceCards) }

                    // 11. Final CTA Banner
                    item { FinalCtaBanner(onEvent) }

                    // 12. Footer
                    item { GlobalWebFooter() }
                }
            }

            // Mega Menu Overlay when active
            state.activeMegaMenu?.let { menu ->
                MegaMenuDropdownOverlay(
                    menuName = menu,
                    onClose = { onEvent(LandingPageEvent.ToggleMegaMenu(null)) },
                    onEvent = onEvent,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 1. Sticky Global Navigation Header & Mega Menu
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun GlobalNavigationHeader(
    activeMegaMenu: String?,
    onEvent: (LandingPageEvent) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onEvent(LandingPageEvent.ToggleMegaMenu(null)) },
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.Bolt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "ZAPMANCER",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Nav Links with Mega Menu Toggles
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listOf("Find Talent", "Find Work", "Solutions", "Resources").forEach { menu ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onEvent(LandingPageEvent.ToggleMegaMenu(menu)) },
                    ) {
                        Text(
                            text = menu,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (activeMegaMenu == menu) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = if (activeMegaMenu == menu) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            // Auth Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = { onEvent(LandingPageEvent.LoginClicked) },
                    shape = MaterialTheme.shapes.pill,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Text("Log in", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { onEvent(LandingPageEvent.SignUpClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = MaterialTheme.shapes.pill,
                ) {
                    Text("Sign up", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MegaMenuDropdownOverlay(
    menuName: String,
    onClose: () -> Unit,
    onEvent: (LandingPageEvent) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = menuName.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (menuName) {
                "Find Talent" -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            MegaMenuItem("Hire Freelancers", "Find skilled professionals for your project") { onEvent(LandingPageEvent.FindTalentClicked) }
                            MegaMenuItem("Post a Project", "Tell us what you need and find the right talent") { onEvent(LandingPageEvent.FindTalentClicked) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            MegaMenuItem("Browse Services", "Explore ready-to-hire packages") { onEvent(LandingPageEvent.FindTalentClicked) }
                            MegaMenuItem("Build a Team", "Bring multiple specialists together") { onEvent(LandingPageEvent.FindTalentClicked) }
                        }
                    }
                }
                "Find Work" -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            MegaMenuItem("Browse Projects", "Discover opportunities matching your skills") { onEvent(LandingPageEvent.FindWorkClicked) }
                            MegaMenuItem("Create Your Profile", "Showcase your experience and expertise") { onEvent(LandingPageEvent.SignUpClicked) }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Explore $menuName solutions tailored for your business needs.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Close Menu",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onClose() }.align(Alignment.End),
            )
        }
    }
}

@Composable
private fun MegaMenuItem(title: String, subtitle: String, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }.padding(vertical = 4.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. Hero Section
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroSection(
    hero: com.smach.zapmancer.domain.model.HeroData,
    onEvent: (LandingPageEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background,
                    ),
                ),
            )
            .padding(horizontal = 32.dp, vertical = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 1000.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = hero.headline,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 54.sp,
                    lineHeight = 62.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = hero.subtext,
                style = MaterialTheme.typography.headlineSmall.copy(
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Normal,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 750.dp),
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { onEvent(LandingPageEvent.FindTalentClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = MaterialTheme.shapes.pill,
                    modifier = Modifier.height(56.dp).width(180.dp),
                ) {
                    Text(hero.primaryCtaText, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                OutlinedButton(
                    onClick = { onEvent(LandingPageEvent.FindWorkClicked) },
                    shape = MaterialTheme.shapes.pill,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.height(56.dp).width(180.dp),
                ) {
                    Text(hero.secondaryCtaText, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                hero.popularRoles.forEach { role ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.pill,
                    ) {
                        Text(
                            role,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Marketplace Visual Flow Card
            MarketplaceFlowGraphic()
        }
    }
}

@Composable
private fun MarketplaceFlowGraphic() {
    Card(
        modifier = Modifier.fillMaxWidth().height(260.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Node 1: Client
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UserAvatar(imageUrl = "", size = 64.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Client / Startup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Post Requirements", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))

            // Node 2: Central Platform Hub
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                    Text("ZAPMANCER", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    Text("AI Matching & Escrow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))

            // Node 3: Freelancers
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                    UserAvatar(imageUrl = "", size = 48.dp)
                    UserAvatar(imageUrl = "", size = 48.dp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Specialists & Teams", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Deliver Excellence", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. Two Paths Section ("Whatever you're building, start here")
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun TwoPathsSection(
    paths: com.smach.zapmancer.domain.model.TwoPathsData,
    onEvent: (LandingPageEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = paths.sectionTitle,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.widthIn(max = 1000.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Path 1: Hiring
            Card(
                modifier = Modifier.weight(1f).clickable { onEvent(LandingPageEvent.FindTalentClicked) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text("I'm Hiring", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(paths.hiringTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(paths.hiringDescription, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(paths.hiringCta, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Path 2: Freelancing
            Card(
                modifier = Modifier.weight(1f).clickable { onEvent(LandingPageEvent.FindWorkClicked) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text("I'm Freelancing", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(paths.freelancingTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(paths.freelancingDescription, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(paths.freelancingCta, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. Products Section ("Everything you need to get work done")
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProductsSection(products: List<ProductCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Everything you need to get work done.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.widthIn(max = 1100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            products.take(3).forEach { product ->
                ProductGridCard(product, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.widthIn(max = 1100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            products.drop(3).take(3).forEach { product ->
                ProductGridCard(product, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ProductGridCard(product: ProductCard, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(180.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(product.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(product.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(product.ctaText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. Solutions Section ("Built for the way you work")
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SolutionsSection(solutions: List<SolutionCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Built for the way you work.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.widthIn(max = 1100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            solutions.forEach { solution ->
                Card(
                    modifier = Modifier.weight(1f).height(220.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(solution.category.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(solution.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(solution.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(solution.ctaText, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. How Zapmancer Works
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HowItWorksSection(steps: List<HowItWorksStep>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("From idea to done.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.widthIn(max = 1100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            steps.forEach { step ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(step.stepNumber, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(step.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(step.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. Marketplace Showcase & Skills
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MarketplaceShowcaseSection(categories: List<MarketplaceCategory>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Talent for every kind of work.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.widthIn(max = 1000.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            categories.forEach { category ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(category.categoryName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            category.popularSkills.forEach { skill ->
                                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.pill) {
                                    Text(skill, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. Featured Talent & Projects
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun FeaturedSection(talent: List<FeaturedTalent>, projects: List<FeaturedProject>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Meet the people behind great work.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.widthIn(max = 1100.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            talent.forEach { item ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        UserAvatar(imageUrl = item.avatarUrl, size = 64.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(item.role, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${item.rating} · ${item.hourlyRate}", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 9. Trust & Safety Section
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun TrustSafetySection(items: List<TrustSafetyItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Work with confidence.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.widthIn(max = 1100.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items.forEach { item ->
                Card(
                    modifier = Modifier.weight(1f).height(160.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 10. Success Stories & Resources
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SuccessStoriesSection(stories: List<SuccessStory>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Great work creates great stories.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.widthIn(max = 1100.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            stories.forEach { story ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(story.subtitle.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(story.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("\"${story.storySnippet}\"", style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourcesSection(resources: List<ResourceCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Learn. Build. Grow.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.widthIn(max = 1100.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            resources.forEach { card ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(card.targetAudience.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(card.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(card.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(card.ctaText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 11. Final CTA Banner & Footer
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun FinalCtaBanner(onEvent: (LandingPageEvent) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Ready to make something happen?",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "The right people. The right opportunities. One place to make great work happen.",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { onEvent(LandingPageEvent.FindTalentClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
                    shape = MaterialTheme.shapes.pill,
                    modifier = Modifier.height(52.dp).width(160.dp),
                ) {
                    Text("Find Talent", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { onEvent(LandingPageEvent.FindWorkClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
                    shape = MaterialTheme.shapes.pill,
                    modifier = Modifier.height(52.dp).width(160.dp),
                ) {
                    Text("Find Work", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun GlobalWebFooter() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(64.dp)) {
            Text("ZAPMANCER", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("The place where talent meets opportunity.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(32.dp))
            Text("© 2026 Zapmancer Inc. All rights reserved. Privacy · Terms · Security", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
