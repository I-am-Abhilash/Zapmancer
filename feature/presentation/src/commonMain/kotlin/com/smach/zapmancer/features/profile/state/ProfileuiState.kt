package com.smach.zapmancer.features.profile.state

data class ProfileUiState(
    val name: String = "Julian Vancore",
    val role: String = "Senior Systems Architect & Interaction Designer",
    val location: String = "San Francisco",
    val ranking: String = "2nd Runner Up",
    val isTopRated: Boolean = true,
    val projectsCount: Int = 124,
    val rating: Double = 9.8,
    val experience: String = "12yr",
    val about: String = "With over 12 years of experience in building mission-critical software architectures, I specialize in bridging the gap between high-density data systems and intuitive user interfaces. My approach is rooted in systematic precision and functional minimalism.",
    val skills: List<String> = listOf(
        "Systems Architecture", "TypeScript", "Rust", "Product Strategy",
        "UI/UX Engineering", "GraphQL", "Cloud Native"
    ),
    val portfolioItems: List<PortfolioItem> = listOf(
        PortfolioItem(
            title = "Aether Financial Dashboard",
            description = "Real-time trading analytics system for institutional investors.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAFHrAeI8xlfoblZPQ8XZAivFQ3g31mF-nCAXGD2fSpxM4npxhFVtP1bvo0-f1OnLpo5q_tgnputiSKramMeUfp-0wqYZzEUx15PjkSeSq5oowMErXaVbhyXYo4L1CEEX8K9NLYidq6A7R9NXgQGMs4KoRf_dbuB21tvf2FArtYZEoW_ZrLG_iewC1k8orzqUseTG4RzmyXrVFfhEMCPwCjgiMA3BkK0Umrd5zKg6HYpvwWS7ZeAE8Z3jCCRp5YfKkzk7eqasKq_go"
        ),
        PortfolioItem(
            title = "Nexus Dev Environment",
            description = "Cloud-native development environment for enterprise teams.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDACYak70sH820Ji5jChGX0-z-9lPf17MMz4BPcs3zJRvt-yHRd-bXvCfFHMJ8NSOM0UqC6Ks9IRRnxbOEqEpP75HVb5JIIbj_0_gFfGaqe6wCuAzMvf6_7WD5fv9g2LdBA83ZRqWQh5FyTghmWXC6QlKI7AOUQ9jTtYDH7Kkfbl5vRTgbmjfIyvC4DNC_-lOC2pBYhI1g4DzWCSqUXlXSsYDoqCXhe_X2wzYiIO8i_A1ILGu3j1zJFr_lkUgylL6oNs_tX2l_DYXk"
        )
    ),
    val reviews: List<ProfileReview> = listOf(
        ProfileReview(
            authorId = "user_sj_123",
            authorName = "Sarah Jenkins",
            authorRole = "CTO at Velocity Flow",
            content = "Julian delivered our entire SaaS architecture ahead of schedule. His ability to understand complex business logic and translate it into a clean, performant system is unmatched. A true professional.",
            rating = 5,
            authorAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAX8tbMna09O86Wf5o2nHWHxsqjy7PARWhxZXBCVEWOe7KfAJ9eTK1JqA5LH7wh8NgBt2Dh6YT7t34Ay3Wm__NSI__FFShGQSbJT4vkBQFPnYTgUToF5QZpYgZESL4TgKbxoPgnYfjj4GMYJzn4J3FI3CapiStdQ4GlZKecwDNJTuDGIfCHXG_De4Gzw8Fr-oziYeoZIy01oCMOTAKtIivyNuH68QFqBjeLpkJAea8JDdWbxSePLlbr5U5_jhrpqzToIO5g-Oz5yfs"
        ),
        ProfileReview(
            authorId = "user_mt_456",
            authorName = "Marcus Thorne",
            authorRole = "Product Director at Core Systems",
            content = "Working with Julian was a seamless experience. He doesn't just write code; he builds solutions that scale. His design eye for high-density dashboards significantly improved our user retention.",
            rating = 5,
            authorAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBz9tTj3_5vGH1u5DqRxdPiVCqU3iw7l3vA6ztvQeKsG2iAVJdmkCAAWVH-_WQF0cWiYSr2K4t3fJn0Tuv5RGp59mgMRB-eGv_CHSPtji1TI5wfX4bxb_RRLrUdouqZqdy7aMr_urFPWPVKrzk6ahoiNsR9v_mMcH5Q6mdBPTkuG-WAhvC0u2iKkvv5PDDVoVtF_ebMjQf2zb9mO7lxY1X40y8T9StVUmq50wcfUSMHEXC_ZR2oucd-bTLC6OFMYq00jZ3m8b9L4Ss"
        )
    ),
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuBrBrKqoM8axW5MPKsBTP5b-rY47j3sPFMPKxLb9MC-OiKc2nVehBkyjSvjrG61iLhnECENazpIX7ZGYdSvJhKpIGWBgn-fNWKLOFOAoJvAOS7uUgeFV7IEUSxjbQHtWEbwQGrVnBP5GX0LOssfjYZWHQOHZeoQNPaT0aZZAB44DcV0MaETyz8F_dFWst5O4bhj6tODWrstc0H0BKuGeulwq4Nbqlg5_5SCdjeZWbq0lUi7AAm8ZezuoaO1rWJpKniR5CNjmrAo9eo",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isHireSuccess: Boolean = false,
    val isOwnProfile: Boolean = true
)

data class PortfolioItem(
    val title: String,
    val description: String,
    val imageUrl: String
)

data class ProfileReview(
    val authorId: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val rating: Int,
    val authorAvatarUrl: String
)