package com.smach.zapmancer.data.di

import com.smach.zapmancer.data.repository.HomeRepositoryImpl
import com.smach.zapmancer.data.repository.ProjectRepositoryImpl
import com.smach.zapmancer.data.repository.ProposalRepositoryImpl
import com.smach.zapmancer.data.repository.SettingsRepositoryImpl
import com.smach.zapmancer.domain.repository.HomeRepository
import com.smach.zapmancer.domain.repository.ProjectRepository
import com.smach.zapmancer.domain.repository.ProposalRepository
import com.smach.zapmancer.domain.repository.SettingsRepository
import com.smach.zapmancer.domain.usecase.ApplyProjectUseCase
import com.smach.zapmancer.domain.usecase.ExportActivityCsvUseCase
import com.smach.zapmancer.domain.usecase.GetHomeDashboardUseCase
import com.smach.zapmancer.domain.usecase.GetProjectDetailUseCase
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.domain.usecase.GetSettingsUseCase
import com.smach.zapmancer.domain.usecase.LogoutUseCase
import com.smach.zapmancer.domain.usecase.SaveProjectUseCase
import com.smach.zapmancer.domain.usecase.SubmitProposalUseCase
import com.smach.zapmancer.domain.usecase.UpdateSettingsUseCase
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
}
