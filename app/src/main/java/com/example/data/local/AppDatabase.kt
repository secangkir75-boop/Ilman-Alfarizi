package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserAccountEntity::class,
        LearningContentEntity::class,
        ExamQuestionEntity::class,
        ExamAttemptEntity::class,
        CertificateEntity::class,
        WritingPracticeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
    abstract fun learningContentDao(): LearningContentDao
    abstract fun examQuestionDao(): ExamQuestionDao
    abstract fun examAttemptDao(): ExamAttemptDao
    abstract fun certificateDao(): CertificateDao
    abstract fun writingPracticeDao(): WritingPracticeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ihyaul_arabiyyah_db"
                )
                    .addCallback(DatabaseSeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseSeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(database)
                }
            }
        }
    }

    suspend fun ensureSeeded() {
        val designerDao = userAccountDao()
        val contentDao = learningContentDao()
        val questionDao = examQuestionDao()

        if (designerDao.getByEmail(SeedData.defaultDesignerAccount().email) == null) {
            designerDao.upsertUser(SeedData.defaultDesignerAccount())
        }
        if (contentDao.count() == 0) {
            SeedData.initialLearningContents().forEach { contentDao.insert(it) }
        } else {
            contentDao.replaceMediaUrl(
                "https://www.youtube.com/results?search_query=belajar+bahasa+arab+makharijul+huruf",
                "https://www.youtube.com/watch?v=sAgFhhFYaEU"
            )
            contentDao.replaceMediaUrl(
                "https://www.youtube.com/results?search_query=percakapan+bahasa+arab+perkenalan+taaruf",
                "https://www.youtube.com/watch?v=QWOtau1vMqw"
            )
        }
        if (questionDao.count() == 0) {
            SeedData.initialExamQuestions().forEach { questionDao.insert(it) }
        }
    }
}

private suspend fun seedDatabase(database: AppDatabase) {
    database.ensureSeeded()
}
