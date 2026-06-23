package com.smach.zapmancer.features.proposal.state

data class ProposalUiState(
    val currentStep: Int = 2,
    val freelancerName: String = "David Chen",
    val freelancerRole: String = "Senior Full-Stack Engineer",
    val freelancerAvatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjFMg3OCPVe9d1lB1l9hl71MZPWakVdK-C15a4F_4AVdPp67wcJ5XvWMvSfxuuYckzhUijJL5bnLAE71mrkxvTkK86X92825FrIi-nSxwLVn6DErAfyevYt2G4-edKucoICEY5cQsA6N_onWKjc--R3JJHqHjyycQGNyKpqLr7_PB8Tmdl7-LunjIsPXKR4oVU6GYagRyPGe2w9_QbmF66kDQyAuCPiq5ftOgYavhljK1Jjg1Hm2BbiXkW-BVwnJIjdeQtjaMlv1E",
    val pitchContent: String = "",
    val budget: String = "232$",
    val timelineDays: String = "23 Days",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val projectType: String = "Fixed Price",
    val estStart: String = "",
)

enum class ProposalStep(val step: Int, val title: String) {
    DETAILS(1, "Details"),
    PITCH(2, "Your Pitch"),
    REVIEW(3, "Review"),
    FINALIZE(4, "Finalize"),
}
