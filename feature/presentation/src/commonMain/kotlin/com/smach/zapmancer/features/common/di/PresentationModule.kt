package com.smach.zapmancer.features.common.di

import com.smach.zapmancer.features.alerts.viewmodel.NotificationViewModel
import com.smach.zapmancer.features.auth.viewmodel.ForgotPasswordViewModel
import com.smach.zapmancer.features.auth.viewmodel.LoginViewModel
import com.smach.zapmancer.features.auth.viewmodel.SignupViewModel
import com.smach.zapmancer.features.auth.viewmodel.VerificationViewModel
import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import com.smach.zapmancer.features.projects.viewmodel.PostProjectViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectDetailViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import com.smach.zapmancer.features.proposal.viewmodel.ClientProposalsViewModel
import com.smach.zapmancer.features.proposal.viewmodel.ProposalViewModel
import com.smach.zapmancer.features.search.viewmodel.SearchViewModel
import com.smach.zapmancer.features.settings.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
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

val alertsModule = module {
    viewModelOf(::NotificationViewModel)
}


val authModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModel { (email: String) -> VerificationViewModel(email, get()) }
    viewModelOf(::ForgotPasswordViewModel)
}


val homeModule = module {
    viewModelOf(::HomeViewModel)
}



val messagesModule = module {
    viewModelOf(::MessagesListViewModel)

    viewModel { (conversationId: String, contactName: String, contactAvatarUrl: String, isOnline: Boolean) ->
        MessagesDetailViewModel(
            conversationId = conversationId,
            contactName = contactName,
            contactAvatarUrl = contactAvatarUrl,
            isOnline = isOnline,
            getMessagesUseCase = get(),
            sendMessageUseCase = get(),
            markConversationAsReadUseCase = get(),
            observePresenceUseCase = get(),
            observeTypingUseCase = get(),
            sendTypingStatusUseCase = get(),
        )
    }
}


val profileModule = module {
    viewModel { (userId: String?) ->
        ProfileViewModel(
            userId = userId,
            getUserProfileUseCase = get(),
            hireUserUseCase = get(),
        )
    }
    viewModel {
        com.smach.zapmancer.features.profile.viewmodel.EditProfileViewModel(
            getUserProfileUseCase = get(),
            updateProfileUseCase = get(),
        )
    }
}


val projectsModule = module {
    viewModelOf(::ProjectListViewModel)
    viewModelOf(::PostProjectViewModel)

    viewModel { (projectId: String) ->
        ProjectDetailViewModel(
            projectId = projectId,
            getProjectDetailUseCase = get(),
            saveProjectUseCase = get(),
            applyProjectUseCase = get(),
        )
    }
}


val proposalModule = module {
    viewModelOf(::ProposalViewModel)

    viewModel { (projectId: String) ->
        ClientProposalsViewModel(
            projectId = projectId,
            getProjectProposalsUseCase = get(),
        )
    }
}



val searchModule = module {
    viewModel {
        SearchViewModel(
            getProjectsUseCase = get(),
        )
    }
}


val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
