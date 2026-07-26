package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.repository.AuthRepository
import org.koin.core.annotation.Factory

@Factory
class SignUpUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
    ): Result<User, DataError.Network> = repository.signUp(email, username, password)
}
