package com.smach.zapmancer.projects.di

import com.smach.zapmancer.projects.data.ProjectsRepository
import com.smach.zapmancer.projects.domain.ProjectsService
import org.koin.dsl.module

val projectsModule = module {
    single { ProjectsRepository() }
    single { ProjectsService(get(), get()) }
}
