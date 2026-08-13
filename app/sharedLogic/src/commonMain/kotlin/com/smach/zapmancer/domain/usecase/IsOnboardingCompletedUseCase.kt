package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class IsOnboardingCompletedUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.isOnboardingCompleted()
}
