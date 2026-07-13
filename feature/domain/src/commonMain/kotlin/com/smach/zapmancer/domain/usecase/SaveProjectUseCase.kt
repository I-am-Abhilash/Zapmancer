package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProjectRepository

class SaveProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String, isSaved: Boolean): Result<Unit, DataError.Network> = repository.saveProject(id, isSaved)
}
