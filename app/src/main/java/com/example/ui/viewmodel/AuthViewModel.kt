package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserAccountEntity
import com.example.data.model.DesignerProfileData
import com.example.data.model.UserRole
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val designerPreviewAsStudent: Boolean = false
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<UserAccountEntity?> = authRepository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val allStudents: StateFlow<List<UserAccountEntity>> = authRepository.allStudents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeRole: StateFlow<UserRole> = currentUser.map { user ->
        if (user?.email == DesignerProfileData.DESIGNER_EMAIL && user.role == "DESIGNER") {
            UserRole.DESIGNER
        } else {
            UserRole.STUDENT
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserRole.STUDENT
    )

    init {
        viewModelScope.launch {
            authRepository.ensureInitialized()
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    fun toggleDesignerStudentPreview(previewStudent: Boolean) {
        _uiState.update { it.copy(designerPreviewAsStudent = previewStudent) }
    }

    fun signIn(email: String, password: String, designerModeOnly: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            val result = authRepository.signIn(email, password, designerModeOnly)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            designerPreviewAsStudent = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal masuk."
                        )
                    }
                }
            )
        }
    }

    fun registerStudent(
        email: String,
        password: String,
        fullName: String,
        schoolOrCampus: String,
        originRegion: String,
        arabicLevel: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            val result = authRepository.registerStudent(
                rawEmail = email,
                rawPassword = password,
                fullName = fullName,
                schoolOrCampus = schoolOrCampus,
                originRegion = originRegion,
                arabicLevel = arabicLevel
            )
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Pendaftaran gagal."
                        )
                    }
                }
            )
        }
    }

    fun resetStudentPassword(email: String, fullName: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            val result = authRepository.resetStudentPassword(email, fullName, newPassword)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            infoMessage = "Kata sandi berhasil diperbarui! Silakan masuk dengan sandi baru."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal mengatur ulang sandi."
                        )
                    }
                }
            )
        }
    }

    fun updateProfile(
        current: UserAccountEntity,
        fullName: String,
        schoolOrCampus: String,
        originRegion: String,
        arabicLevel: String,
        phone: String,
        learningGoal: String
    ) {
        viewModelScope.launch {
            val updated = current.copy(
                fullName = fullName.trim().ifBlank { current.fullName },
                schoolOrCampus = schoolOrCampus.trim(),
                originRegion = originRegion.trim(),
                arabicLevel = arabicLevel.trim(),
                phone = phone.trim(),
                learningGoal = learningGoal.trim()
            )
            authRepository.updateProfile(updated)
            _uiState.update { it.copy(infoMessage = "Data diri pelajar berhasil disimpan.") }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { AuthUiState() }
        }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}
