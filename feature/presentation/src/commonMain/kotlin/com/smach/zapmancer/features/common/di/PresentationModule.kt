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
import com.smach.zapmancer.features.settings.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::SignupViewModel)
        viewModelOf(::VerificationViewModel)
        viewModelOf(::ForgotPasswordViewModel)
        viewModel { (userId: String?) ->
            ProfileViewModel(
                userId = userId,
                getUserProfileUseCase = get(),
                hireUserUseCase = get(),
            )
        }
        viewModelOf(::NotificationViewModel)
        viewModelOf(::MessagesListViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::ProjectListViewModel)
        viewModelOf(::ProposalViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::PostProjectViewModel)
        viewModel { (conversationId: String) ->
            MessagesDetailViewModel(
                conversationId = conversationId,
                getMessagesUseCase = get(),
                sendMessageUseCase = get(),
                markConversationAsReadUseCase = get(),
            )
        }
        viewModel { (projectId: String) ->
            ProjectDetailViewModel(
                projectId = projectId,
                getProjectDetailUseCase = get(),
                saveProjectUseCase = get(),
                applyProjectUseCase = get(),
            )
        }
        viewModel { (projectId: String) ->
            ClientProposalsViewModel(
                projectId = projectId,
                getProjectProposalsUseCase = get(),
            )
        }
    }
