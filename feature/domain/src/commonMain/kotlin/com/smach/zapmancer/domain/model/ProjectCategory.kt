package com.smach.zapmancer.domain.model

enum class ProjectCategory(val displayName: String) {
    ALL("All"),
    DEVELOPMENT("Development"),
    DESIGN("Design"),
    MARKETING("Marketing"),
    WRITING("Writing & Translation"),
    MULTIMEDIA("Video & Animation"),
    CONSULTING("Business & Consulting"),
    ADMIN("Admin Support"),
    FINANCE("Finance & Accounting"),
    LEGAL("Legal & Compliance"),
    ANALYTICS("Data Science & Analytics"),
    SECURITY("Cybersecurity & IT"),
    CUSTOMER_SUPPORT("Customer Support"),
    ;

    companion object {
        fun from(value: String): ProjectCategory = entries.firstOrNull {
            it.name.equals(value, ignoreCase = true)
        } ?: DEVELOPMENT
    }
}
