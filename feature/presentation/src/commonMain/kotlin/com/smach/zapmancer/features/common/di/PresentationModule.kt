package com.smach.zapmancer.features.common.di

import com.smach.zapmancer.features.alerts.viewmodel.NotificationViewModel
import com.smach.zapmancer.features.auth.viewmodel.ForgotPasswordViewModel
import com.smach.zapmancer.features.auth.viewmodel.LoginViewModel
import com.smach.zapmancer.features.auth.viewmodel.SignupViewModel
import com.smach.zapmancer.features.auth.viewmodel.VerificationViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::SignupViewModel)
        viewModelOf(::VerificationViewModel)
        viewModelOf(::ForgotPasswordViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::NotificationViewModel)
        viewModelOf(::MessagesListViewModel)
        viewModel { (conversationId: String) ->
            MessagesDetailViewModel(
                conversationId = conversationId,
                getMessagesUseCase = get(),
                sendMessageUseCase = get(),
                markConversationAsReadUseCase = get(),
            )
        }
    }