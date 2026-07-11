package com.smach.zapmancer.features.search.di

import com.smach.zapmancer.features.search.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel {
        SearchViewModel(
            getProjectsUseCase = get()
        )
    }
}
