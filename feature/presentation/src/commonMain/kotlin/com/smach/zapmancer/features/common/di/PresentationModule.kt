package com.smach.zapmancer.features.common.di

import com.smach.zapmancer.features.auth.viewmodel.ForgotPasswordViewModel
import com.smach.zapmancer.features.auth.viewmodel.LoginViewModel
import com.smach.zapmancer.features.auth.viewmodel.SignupViewModel
import com.smach.zapmancer.features.auth.viewmodel.VerificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::SignupViewModel)
        viewModelOf(::VerificationViewModel)
        viewModelOf(::ForgotPasswordViewModel)
//        viewModel { (articleId: Int) ->
//            DetailViewModel(
//                articleId = articleId,
//                articleRepository = get(),
//            )
//        }
//        viewModelOf(::HomeViewModel)
//        viewModelOf(::ProfileViewModel)
//        viewModelOf(::SearchViewModel)
//        viewModelOf(::WritingViewModel)
    }