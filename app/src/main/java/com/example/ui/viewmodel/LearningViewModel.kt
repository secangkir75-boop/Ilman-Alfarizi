package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.CertificateEntity
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamQuestionEntity
import com.example.data.local.LearningContentEntity
import com.example.data.repository.LearningRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LearningUiState(
    val feedbackMessage: String? = null,
    val lastExamAttempt: ExamAttemptEntity? = null
)

class LearningViewModel(private val repository: LearningRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    private val _activeStudentEmail = MutableStateFlow<String?>(null)

    fun bindStudentEmail(email: String?) {
        _activeStudentEmail.value = email?.trim()?.lowercase()
    }

    val allContents: StateFlow<List<LearningContentEntity>> = repository.allContents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuestions: StateFlow<List<ExamQuestionEntity>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExamAttempts: StateFlow<List<ExamAttemptEntity>> = repository.allExamAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCertificates: StateFlow<List<CertificateEntity>> = repository.allCertificates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentAttempts: StateFlow<List<ExamAttemptEntity>> = _activeStudentEmail
        .flatMapLatest { email ->
            if (email.isNullOrBlank()) flowOf(emptyList()) else repository.observeStudentAttempts(email)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentCertificates: StateFlow<List<CertificateEntity>> = _activeStudentEmail
        .flatMapLatest { email ->
            if (email.isNullOrBlank()) flowOf(emptyList()) else repository.observeStudentCertificates(email)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearFeedback() {
        _uiState.update { it.copy(feedbackMessage = null) }
    }

    fun clearLastExamAttempt() {
        _uiState.update { it.copy(lastExamAttempt = null) }
    }

    fun saveContent(content: LearningContentEntity) {
        viewModelScope.launch {
            repository.saveContent(content)
            _uiState.update { it.copy(feedbackMessage = "Materi '${content.title}' berhasil disimpan.") }
        }
    }

    fun deleteContent(id: Int) {
        viewModelScope.launch {
            repository.deleteContent(id)
            _uiState.update { it.copy(feedbackMessage = "Materi berhasil dihapus.") }
        }
    }

    fun saveQuestion(question: ExamQuestionEntity) {
        viewModelScope.launch {
            repository.saveQuestion(question)
            _uiState.update { it.copy(feedbackMessage = "Soal ujian berhasil disimpan.") }
        }
    }

    fun deleteQuestion(id: Int) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
            _uiState.update { it.copy(feedbackMessage = "Soal ujian berhasil dihapus.") }
        }
    }

    fun submitExam(
        studentEmail: String,
        studentName: String,
        questions: List<ExamQuestionEntity>,
        selectedAnswers: Map<Int, Int>
    ) {
        if (questions.isEmpty()) return
        viewModelScope.launch {
            val correctCount = questions.count { q ->
                selectedAnswers[q.id] == q.correctOptionIndex
            }
            val attempt = repository.submitExamAttempt(
                studentEmail = studentEmail,
                studentName = studentName,
                correctCount = correctCount,
                totalQuestions = questions.size
            )
            _uiState.update {
                it.copy(
                    lastExamAttempt = attempt,
                    feedbackMessage = "Ujian selesai! Nilai Anda: ${attempt.score} (${attempt.predicate})."
                )
            }
        }
    }

    fun issueCertificate(
        studentEmail: String,
        studentName: String,
        examAttemptId: Int,
        score: Int,
        predicate: String,
        designerNote: String
    ) {
        viewModelScope.launch {
            repository.issueCertificate(
                studentEmail = studentEmail,
                studentName = studentName,
                examAttemptId = examAttemptId,
                score = score,
                predicate = predicate,
                designerNote = designerNote
            )
            _uiState.update {
                it.copy(feedbackMessage = "Sertifikat predikat $predicate berhasil dikirim ke $studentName.")
            }
        }
    }

    fun acceptCertificate(certificateId: Int) {
        viewModelScope.launch {
            repository.acceptCertificate(certificateId)
            _uiState.update {
                it.copy(feedbackMessage = "Alhamdulillah! Sertifikat resmi telah Anda terima.")
            }
        }
    }

    fun deleteCertificate(certificateId: Int) {
        viewModelScope.launch {
            repository.deleteCertificate(certificateId)
            _uiState.update { it.copy(feedbackMessage = "Sertifikat dihapus.") }
        }
    }

    fun savePractice(studentEmail: String, contentId: Int, category: String, responseText: String) {
        viewModelScope.launch {
            repository.savePractice(studentEmail, contentId, category, responseText)
            _uiState.update { it.copy(feedbackMessage = "Latihan Anda tersimpan.") }
        }
    }

    class Factory(private val repository: LearningRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LearningViewModel(repository) as T
        }
    }
}
