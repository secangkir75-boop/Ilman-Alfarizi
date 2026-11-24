package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.SessionManager
import com.example.data.model.DesignerProfileData
import com.example.data.model.ExamPredicate
import com.example.data.repository.AuthRepository
import com.example.ui.components.VideoUrlParser
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        authRepository = AuthRepository(db, SessionManager(context))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun `read string from context matches app name`() {
        val appName = context.getString(R.string.app_name)
        assertEquals("Iḥyā’ul ‘Arabiyyah", appName)
    }

    @Test
    fun `exam predicate mapping covers makbul to mumtaz`() {
        assertEquals(ExamPredicate.MUMTAZ, ExamPredicate.fromScore(95))
        assertEquals(ExamPredicate.JAYYID_JIDDAN, ExamPredicate.fromScore(85))
        assertEquals(ExamPredicate.JAYYID, ExamPredicate.fromScore(75))
        assertEquals(ExamPredicate.MAKBUL, ExamPredicate.fromScore(65))
        assertEquals(ExamPredicate.BELUM_LULUS, ExamPredicate.fromScore(45))
    }

    @Test
    fun `designer exclusive sign in works only for secangkir75 with pecinta kopi`() = runTest {
        val success = authRepository.signIn(
            rawEmail = DesignerProfileData.DESIGNER_EMAIL,
            rawPassword = DesignerProfileData.DESIGNER_PASSPHRASE,
            designerModeOnly = true
        )
        assertTrue(success.isSuccess)
        assertEquals("DESIGNER", success.getOrNull()?.role)

        val wrongEmail = authRepository.signIn(
            rawEmail = "pelajar@gmail.com",
            rawPassword = DesignerProfileData.DESIGNER_PASSPHRASE,
            designerModeOnly = true
        )
        assertTrue(wrongEmail.isFailure)

        val wrongPass = authRepository.signIn(
            rawEmail = DesignerProfileData.DESIGNER_EMAIL,
            rawPassword = "salah",
            designerModeOnly = true
        )
        assertTrue(wrongPass.isFailure)
    }

    @Test
    fun `student can register new in-app email and cannot hijack designer email`() = runTest {
        val studentResult = authRepository.registerStudent(
            rawEmail = "ahmad123@ihya.arab",
            rawPassword = "aman",
            fullName = "Ahmad Fauzan",
            schoolOrCampus = "MAN 1 Mataram",
            originRegion = "Lombok",
            arabicLevel = "Menengah"
        )
        assertTrue(studentResult.isSuccess)
        assertEquals("STUDENT", studentResult.getOrNull()?.role)

        val hijackResult = authRepository.registerStudent(
            rawEmail = DesignerProfileData.DESIGNER_EMAIL,
            rawPassword = "hack",
            fullName = "Bukan Perancang",
            schoolOrCampus = "",
            originRegion = "",
            arabicLevel = "Pemula (Mubtadi')"
        )
        assertTrue(hijackResult.isFailure)
    }

    @Test
    fun `youtube url parser extracts id for in-app playback`() {
        assertEquals("sAgFhhFYaEU", VideoUrlParser.extractYouTubeId("https://www.youtube.com/watch?v=sAgFhhFYaEU"))
        assertEquals("QWOtau1vMqw", VideoUrlParser.extractYouTubeId("https://youtu.be/QWOtau1vMqw?si=abc"))
        assertEquals("gr2TdD4uIsI", VideoUrlParser.extractYouTubeId("https://www.youtube.com/shorts/gr2TdD4uIsI"))
        assertEquals("sAgFhhFYaEU", VideoUrlParser.extractYouTubeId("https://www.youtube.com/embed/sAgFhhFYaEU"))
    }
}
