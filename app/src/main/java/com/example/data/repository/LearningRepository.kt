package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CertificateEntity
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamQuestionEntity
import com.example.data.local.LearningContentEntity
import com.example.data.local.WritingPracticeEntity
import com.example.data.model.DesignerProfileData
import com.example.data.model.ExamPredicate
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class LearningRepository(private val database: AppDatabase) {
    private val contentDao = database.learningContentDao()
    private val questionDao = database.examQuestionDao()
    private val attemptDao = database.examAttemptDao()
    private val certificateDao = database.certificateDao()
    private val practiceDao = database.writingPracticeDao()

    val allContents: Flow<List<LearningContentEntity>> = contentDao.observeAll()
    val allQuestions: Flow<List<ExamQuestionEntity>> = questionDao.observeAll()
    val allExamAttempts: Flow<List<ExamAttemptEntity>> = attemptDao.observeAll()
    val allCertificates: Flow<List<CertificateEntity>> = certificateDao.observeAll()

    fun observeByCategory(category: String): Flow<List<LearningContentEntity>> =
        contentDao.observeByCategory(category)

    fun observeStudentAttempts(email: String): Flow<List<ExamAttemptEntity>> =
        attemptDao.observeByStudent(email)

    fun observeStudentCertificates(email: String): Flow<List<CertificateEntity>> =
        certificateDao.observeByStudent(email)

    fun observePractice(email: String, contentId: Int): Flow<WritingPracticeEntity?> =
        practiceDao.observeForContent(email, contentId)

    suspend fun saveContent(content: LearningContentEntity) {
        if (content.id == 0) {
            contentDao.insert(content)
        } else {
            contentDao.update(content)
        }
    }

    suspend fun deleteContent(id: Int) = contentDao.deleteById(id)

    suspend fun saveQuestion(question: ExamQuestionEntity) {
        if (question.id == 0) {
            questionDao.insert(question)
        } else {
            questionDao.update(question)
        }
    }

    suspend fun deleteQuestion(id: Int) = questionDao.deleteById(id)

    suspend fun submitExamAttempt(
        studentEmail: String,
        studentName: String,
        correctCount: Int,
        totalQuestions: Int
    ): ExamAttemptEntity {
        val safeTotal = totalQuestions.coerceAtLeast(1)
        val score = ((correctCount.toFloat() / safeTotal) * 100f).toInt().coerceIn(0, 100)
        val predicate = ExamPredicate.fromScore(score).displayName
        val attempt = ExamAttemptEntity(
            studentEmail = studentEmail,
            studentName = studentName,
            score = score,
            correctCount = correctCount,
            totalQuestions = safeTotal,
            predicate = predicate
        )
        val insertedId = attemptDao.insert(attempt).toInt()
        return attempt.copy(id = insertedId)
    }

    suspend fun issueCertificate(
        studentEmail: String,
        studentName: String,
        examAttemptId: Int,
        score: Int,
        predicate: String,
        designerNote: String
    ) {
        val suffix = (1000..9999).random()
        val code = String.format(Locale.US, "IHYA-2026-%d", suffix)
        val cert = CertificateEntity(
            certificateCode = code,
            studentEmail = studentEmail.trim().lowercase(),
            studentName = studentName.trim(),
            examAttemptId = examAttemptId,
            score = score,
            predicate = predicate,
            designerNote = designerNote.trim(),
            issuedBy = DesignerProfileData.NAME,
            isAcceptedByStudent = false
        )
        certificateDao.insert(cert)
    }

    suspend fun acceptCertificate(certificateId: Int) {
        certificateDao.acceptCertificate(certificateId)
    }

    suspend fun deleteCertificate(certificateId: Int) {
        certificateDao.deleteById(certificateId)
    }

    suspend fun savePractice(
        studentEmail: String,
        contentId: Int,
        category: String,
        responseText: String
    ) {
        practiceDao.upsert(
            WritingPracticeEntity(
                studentEmail = studentEmail,
                contentId = contentId,
                category = category,
                responseText = responseText
            )
        )
    }
}
