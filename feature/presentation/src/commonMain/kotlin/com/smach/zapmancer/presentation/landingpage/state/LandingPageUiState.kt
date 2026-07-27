package com.smach.zapmancer.presentation.landingpage.state

import com.smach.zapmancer.domain.model.LandingPageData

data class LandingPageUiState(
    val isLoading: Boolean = false,
    val data: LandingPageData? = null,
    val activeMegaMenu: String? = null, // "Find Talent", "Find Work", "Solutions", "Resources"
    val error: String? = null,
)
