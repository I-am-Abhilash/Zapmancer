package com.smach.zapmancer.features.common.di

import com.smach.zapmancer.features.alerts.di.alertsModule
import com.smach.zapmancer.features.auth.di.authModule
import com.smach.zapmancer.features.home.di.homeModule
import com.smach.zapmancer.features.messages.di.messagesModule
import com.smach.zapmancer.features.profile.di.profileModule
import com.smach.zapmancer.features.projects.di.projectsModule
import com.smach.zapmancer.features.proposal.di.proposalModule
import com.smach.zapmancer.features.settings.di.settingsModule
import com.smach.zapmancer.features.search.di.searchModule
import org.koin.dsl.module

/**
 * Aggregator for all presentation-layer modules. Individual VM registrations
 * live in their feature's `di/<Feature>Module.kt`.
 */
val presentationModule = module {
    includes(
        authModule,
        homeModule,
        projectsModule,
        proposalModule,
        messagesModule,
        alertsModule,
        profileModule,
        settingsModule,
        searchModule,
    )
}
