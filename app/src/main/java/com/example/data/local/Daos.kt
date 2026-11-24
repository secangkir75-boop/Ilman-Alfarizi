package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    fun observeByEmail(email: String): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE role = 'STUDENT' ORDER BY createdAt DESC")
    fun observeAllStudents(): Flow<List<UserAccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserAccountEntity)

    @Update
    suspend fun updateUser(user: UserAccountEntity)
}

@Dao
interface LearningContentDao {
    @Query("SELECT * FROM learning_contents ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<LearningContentEntity>>

    @Query("SELECT * FROM learning_contents WHERE category = :category ORDER BY createdAt DESC")
    fun observeByCategory(category: String): Flow<List<LearningContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: LearningContentEntity)

    @Update
    suspend fun update(content: LearningContentEntity)

    @Query("DELETE FROM learning_contents WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM learning_contents")
    suspend fun count(): Int

    @Query("UPDATE learning_contents SET mediaUrl = :newUrl WHERE mediaUrl = :oldUrl")
    suspend fun replaceMediaUrl(oldUrl: String, newUrl: String)
}

@Dao
interface ExamQuestionDao {
    @Query("SELECT * FROM exam_questions ORDER BY id ASC")
    fun observeAll(): Flow<List<ExamQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: ExamQuestionEntity)

    @Update
    suspend fun update(question: ExamQuestionEntity)

    @Query("DELETE FROM exam_questions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM exam_questions")
    suspend fun count(): Int
}

@Dao
interface ExamAttemptDao {
    @Query("SELECT * FROM exam_attempts WHERE studentEmail = :email ORDER BY submittedAt DESC")
    fun observeByStudent(email: String): Flow<List<ExamAttemptEntity>>

    @Query("SELECT * FROM exam_attempts ORDER BY submittedAt DESC")
    fun observeAll(): Flow<List<ExamAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attempt: ExamAttemptEntity): Long
}

@Dao
interface CertificateDao {
    @Query("SELECT * FROM certificates WHERE studentEmail = :email ORDER BY issuedAt DESC")
    fun observeByStudent(email: String): Flow<List<CertificateEntity>>

    @Query("SELECT * FROM certificates ORDER BY issuedAt DESC")
    fun observeAll(): Flow<List<CertificateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(certificate: CertificateEntity)

    @Query("UPDATE certificates SET isAcceptedByStudent = 1, acceptedAt = :acceptedAt WHERE id = :id")
    suspend fun acceptCertificate(id: Int, acceptedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM certificates WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface WritingPracticeDao {
    @Query("SELECT * FROM writing_practices WHERE studentEmail = :email AND contentId = :contentId LIMIT 1")
    fun observeForContent(email: String, contentId: Int): Flow<WritingPracticeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(practice: WritingPracticeEntity)
}
