package com.smach.zapmancer.domain.model

data class CreateProjectParams(
    val category: String,
    val title: String,
    val description: String,
    val budgetRange: String,
    val timeline: String,
    val deliverables: List<String>,
    val skills: List<String>,
)
