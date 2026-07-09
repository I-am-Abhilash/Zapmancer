package com.smach.zapmancer.proposal.di

import com.smach.zapmancer.proposal.data.ProposalsRepository
import com.smach.zapmancer.proposal.domain.ProposalsService
import org.koin.dsl.module

val proposalsModule = module {
    single { ProposalsRepository() }
    single { ProposalsService(get()) }
}
