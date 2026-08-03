package com.smach.zapmancer.presentation.common.di

import com.smach.zapmancer.presentation.auth.viewmodel.ForgotPasswordViewModel
import com.smach.zapmancer.presentation.auth.viewmodel.LoginViewModel
import com.smach.zapmancer.presentation.auth.viewmodel.SignupViewModel
import com.smach.zapmancer.presentation.auth.viewmodel.VerificationViewModel
import com.smach.zapmancer.presentation.home.viewmodel.HomeViewModel
import com.smach.zapmancer.presentation.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.presentation.messages.viewmodel.MessagesListViewModel
import com.smach.zapmancer.presentation.profile.viewmodel.ProfileViewModel
import com.smach.zapmancer.presentation.projects.viewmodel.PostProjectViewModel
import com.smach.zapmancer.presentation.projects.viewmodel.ProjectDetailViewModel
import com.smach.zapmancer.presentation.projects.viewmodel.ProjectListViewModel
import com.smach.zapmancer.presentation.proposal.viewmodel.ClientProposalsViewModel
import com.smach.zapmancer.presentation.proposal.viewmodel.ProposalViewModel
import com.smach.zapmancer.presentation.search.viewmodel.SearchViewModel
import com.smach.zapmancer.presentation.settings.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

