package com.smach.zapmancer.features.profile.di

import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { (userId: String?) ->
        ProfileViewModel(
            userId = userId,
            getUserProfileUseCase = get(),
            hireUserUseCase = get(),
        )
    }
}
