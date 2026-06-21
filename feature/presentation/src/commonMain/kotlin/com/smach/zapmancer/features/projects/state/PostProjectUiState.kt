package com.smach.zapmancer.features.projects.state

data class PostProjectUiState(
    val title: String = "",
    val category: String = "Development",
    val description: String = "",
    val budgetRange: String = "",
    val timeline: String = "",
    val deliverables: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val currentDeliverableInput: String = "",
    val currentSkillInput: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null
)
