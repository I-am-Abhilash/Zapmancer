package com.smach.zapmancer.data.di

import com.smach.zapmancer.data.repository.AuthRepositoryImpl
import com.smach.zapmancer.data.repository.HomeRepositoryImpl
import com.smach.zapmancer.data.repository.MessageRepositoryImpl
import com.smach.zapmancer.data.repository.NotificationRepositoryImpl
import com.smach.zapmancer.data.repository.ProfileRepositoryImpl
import com.smach.zapmancer.data.repository.ProjectRepositoryImpl
import com.smach.zapmancer.data.repository.ProposalRepositoryImpl
import com.smach.zapmancer.data.repository.SettingsRepositoryImpl
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.domain.repository.HomeRepository
import com.smach.zapmancer.domain.repository.MessageRepository
import com.smach.zapmancer.domain.repository.NotificationRepository
import com.smach.zapmancer.domain.repository.ProfileRepository
import com.smach.zapmancer.domain.repository.ProjectRepository
import com.smach.zapmancer.domain.repository.ProposalRepository
import com.smach.zapmancer.domain.repository.SettingsRepository
import com.smach.zapmancer.domain.usecase.ApplyProjectUseCase
import com.smach.zapmancer.domain.usecase.ExecuteNotificationActionUseCase
import com.smach.zapmancer.domain.usecase.ExportActivityCsvUseCase
import com.smach.zapmancer.domain.usecase.ForgotPasswordUseCase
import com.smach.zapmancer.domain.usecase.GetConversationsUseCase
import com.smach.zapmancer.domain.usecase.GetHomeDashboardUseCase
import com.smach.zapmancer.domain.usecase.GetMessagesUseCase
import com.smach.zapmancer.domain.usecase.GetNotificationsUseCase
import com.smach.zapmancer.domain.usecase.GetProjectDetailUseCase
import com.smach.zapmancer.domain.usecase.GetProjectProposalsUseCase
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.domain.usecase.GetSettingsUseCase
import com.smach.zapmancer.domain.usecase.GetUserProfileUseCase
import com.smach.zapmancer.domain.usecase.HireUserUseCase
import com.smach.zapmancer.domain.usecase.LoginUseCase
import com.smach.zapmancer.domain.usecase.LogoutUseCase
import com.smach.zapmancer.domain.usecase.MarkConversationAsReadUseCase
import com.smach.zapmancer.domain.usecase.PostProjectUseCase
import com.smach.zapmancer.domain.usecase.SaveProjectUseCase
import com.smach.zapmancer.domain.usecase.SendMessageUseCase
import com.smach.zapmancer.domain.usecase.SendNotificationQuickReplyUseCase
import com.smach.zapmancer.domain.usecase.SignUpUseCase
import com.smach.zapmancer.domain.usecase.SubmitProposalUseCase
import com.smach.zapmancer.domain.usecase.UpdateProfileUseCase
import com.smach.zapmancer.domain.usecase.UpdateSettingsUseCase
import com.smach.zapmancer.domain.usecase.VerifyOtpUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::HomeRepositoryImpl) { bind<HomeRepository>() }
    factoryOf(::GetHomeDashboardUseCase)
    factoryOf(::ExportActivityCsvUseCase)

    singleOf(::ProjectRepositoryImpl) { bind<ProjectRepository>() }
    factoryOf(::GetProjectsUseCase)
    factoryOf(::GetProjectDetailUseCase)
    factoryOf(::SaveProjectUseCase)
    factoryOf(::ApplyProjectUseCase)

    singleOf(::ProposalRepositoryImpl) { bind<ProposalRepository>() }
    factoryOf(::SubmitProposalUseCase)

    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    factoryOf(::GetSettingsUseCase)
    factoryOf(::UpdateSettingsUseCase)
    factoryOf(::LogoutUseCase)

    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    factoryOf(::LoginUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::ForgotPasswordUseCase)
    factoryOf(::VerifyOtpUseCase)

    singleOf(::MessageRepositoryImpl) { bind<MessageRepository>() }
    factoryOf(::GetConversationsUseCase)
    factoryOf(::GetMessagesUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::MarkConversationAsReadUseCase)

    singleOf(::NotificationRepositoryImpl) { bind<NotificationRepository>() }
    factoryOf(::GetNotificationsUseCase)
    factoryOf(::ExecuteNotificationActionUseCase)
    factoryOf(::SendNotificationQuickReplyUseCase)

    singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }
    factoryOf(::GetUserProfileUseCase)
    factoryOf(::HireUserUseCase)
    factoryOf(::UpdateProfileUseCase)

    factoryOf(::PostProjectUseCase)
    factoryOf(::GetProjectProposalsUseCase)
}
