package com.smach.zapmancer.proposals.di

import com.smach.zapmancer.proposals.data.ProposalsRepository
import com.smach.zapmancer.proposals.domain.ProposalsService
import org.koin.dsl.module

val proposalsModule = module {
    single { ProposalsRepository() }
    single { ProposalsService(get()) }
}
