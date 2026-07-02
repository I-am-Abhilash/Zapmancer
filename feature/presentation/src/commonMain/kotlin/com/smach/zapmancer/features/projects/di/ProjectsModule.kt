package com.smach.zapmancer.features.projects.di

import com.smach.zapmancer.features.projects.viewmodel.PostProjectViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectDetailViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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
