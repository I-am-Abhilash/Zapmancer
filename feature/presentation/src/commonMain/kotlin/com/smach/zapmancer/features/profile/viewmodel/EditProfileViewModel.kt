package com.smach.zapmancer.features.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.UpdateProfileParams
import com.smach.zapmancer.domain.usecase.GetUserProfileUseCase
import com.smach.zapmancer.domain.usecase.UpdateProfileUseCase
import com.smach.zapmancer.features.profile.state.EditProfileUiState
import kotlinx.coroutines.launch

sealed interface EditProfileEvent {
    data object LoadProfile : EditProfileEvent
    data class NameChanged(val name: String) : EditProfileEvent
    data class RoleTitleChanged(val role: String) : EditProfileEvent
    data class LocationChanged(val location: String) : EditProfileEvent
    data class ExperienceChanged(val experience: String) : EditProfileEvent
    data class AboutChanged(val about: String) : EditProfileEvent
    data class AvatarUrlChanged(val avatarUrl: String) : EditProfileEvent
    data class AddSkill(val skill: String) : EditProfileEvent
    data class RemoveSkill(val skill: String) : EditProfileEvent
    data object SaveProfile : EditProfileEvent
    data object BackClicked : EditProfileEvent
}

sealed interface EditProfileEffect {
    data class ShowToast(val message: String) : EditProfileEffect
    data object NavigateBack : EditProfileEffect
}

class EditProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : BaseViewModel<EditProfileUiState, EditProfileEvent, EditProfileEffect>(EditProfileUiState()) {

    init {
        loadProfile()
    }

    override fun onEvent(event: EditProfileEvent) {
        when (event) {
            EditProfileEvent.LoadProfile -> loadProfile()
            is EditProfileEvent.NameChanged -> updateState { copy(name = event.name) }
            is EditProfileEvent.RoleTitleChanged -> updateState { copy(roleTitle = event.role) }
            is EditProfileEvent.LocationChanged -> updateState { copy(location = event.location) }
            is EditProfileEvent.ExperienceChanged -> updateState { copy(experience = event.experience) }
            is EditProfileEvent.AboutChanged -> updateState { copy(about = event.about) }
            is EditProfileEvent.AvatarUrlChanged -> updateState { copy(avatarUrl = event.avatarUrl) }
            is EditProfileEvent.AddSkill -> addSkill(event.skill)
            is EditProfileEvent.RemoveSkill -> removeSkill(event.skill)
            EditProfileEvent.SaveProfile -> saveProfile()
            EditProfileEvent.BackClicked -> sendEffect(EditProfileEffect.NavigateBack)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getUserProfileUseCase(null).foldTyped(
                onSuccess = { profile ->
                    updateState {
                        copy(
                            name = profile.name,
                            roleTitle = profile.role,
                            location = profile.location.orEmpty(),
                            experience = profile.experience.orEmpty(),
                            about = profile.about.orEmpty(),
                            skills = profile.skills,
                            avatarUrl = profile.avatarUrl.orEmpty(),
                            isLoading = false,
                        )
                    }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(EditProfileEffect.ShowToast("Failed to load profile: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun addSkill(skill: String) {
        val trimmed = skill.trim()
        if (trimmed.isEmpty()) return
        val current = uiState.value.skills
        if (!current.contains(trimmed)) {
            updateState { copy(skills = current + trimmed) }
        }
    }

    private fun removeSkill(skill: String) {
        val current = uiState.value.skills
        updateState { copy(skills = current - skill) }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val state = uiState.value
            updateState { copy(isLoading = true, error = null) }
            val params = UpdateProfileParams(
                name = state.name.ifBlank { null },
                roleTitle = state.roleTitle.ifBlank { null },
                location = state.location.ifBlank { null },
                about = state.about.ifBlank { null },
                experience = state.experience.ifBlank { null },
                skills = state.skills,
                avatarUrl = state.avatarUrl.ifBlank { null },
            )
            updateProfileUseCase(params).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false, isSaveSuccess = true) }
                    sendEffect(EditProfileEffect.ShowToast("Profile saved successfully!"))
                    sendEffect(EditProfileEffect.NavigateBack)
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(EditProfileEffect.ShowToast("Failed to save profile: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
