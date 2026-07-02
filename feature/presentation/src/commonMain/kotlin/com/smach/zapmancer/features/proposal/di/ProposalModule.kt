package com.smach.zapmancer.features.proposal.di

import com.smach.zapmancer.features.proposal.viewmodel.ClientProposalsViewModel
import com.smach.zapmancer.features.proposal.viewmodel.ProposalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val proposalModule = module {
    viewModelOf(::ProposalViewModel)

    viewModel { (projectId: String) ->
        ClientProposalsViewModel(
            projectId = projectId,
            getProjectProposalsUseCase = get(),
        )
    }
}
