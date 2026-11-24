package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val role: String, // "STUDENT" or "DESIGNER"
    val fullName: String,
    val schoolOrCampus: String = "",
    val originRegion: String = "",
    val arabicLevel: String = "Pemula (Mubtadi')",
    val phone: String = "",
    val learningGoal: String = "Menjaga dan meningkatkan kemahiran Bahasa Arab",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "learning_contents")
data class LearningContentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // LearningCategory.name
    val title: String,
    val arabicText: String,
    val transliteration: String = "",
    val indonesianTranslation: String,
    val explanation: String,
    val mediaUrl: String = "",
    val level: String = "Dasar",
    val authorEmail: String = "secangkir75@gmail.com",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_questions")
data class ExamQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionText: String,
    val arabicPrompt: String = "",
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0..3
    val explanation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentEmail: String,
    val studentName: String,
    val score: Int,
    val correctCount: Int,
    val totalQuestions: Int,
    val predicate: String,
    val submittedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "certificates")
data class CertificateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val certificateCode: String,
    val studentEmail: String,
    val studentName: String,
    val examAttemptId: Int = 0,
    val score: Int,
    val predicate: String,
    val designerNote: String = "",
    val issuedBy: String = "Ilman Alfarizi",
    val issuedAt: Long = System.currentTimeMillis(),
    val isAcceptedByStudent: Boolean = false,
    val acceptedAt: Long? = null
)

@Entity(tableName = "writing_practices")
data class WritingPracticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentEmail: String,
    val contentId: Int,
    val category: String,
    val responseText: String,
    val updatedAt: Long = System.currentTimeMillis()
)
