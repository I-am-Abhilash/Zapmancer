package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProjectRepository
import org.koin.core.annotation.Factory

@Factory
class DeleteProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit, DataError.Network> = repository.deleteProject(id)
}
