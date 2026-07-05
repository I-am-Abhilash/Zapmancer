package com.smach.zapmancer.features.messages.di

import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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
        )
    }
}
