package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SecurityUtils
import com.example.data.local.SessionManager
import com.example.data.local.UserAccountEntity
import com.example.data.model.DesignerProfileData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class AuthRepository(
    private val database: AppDatabase,
    private val sessionManager: SessionManager
) {
    private val userDao = database.userAccountDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: Flow<UserAccountEntity?> = sessionManager.currentUserEmail.flatMapLatest { email ->
        if (email.isNullOrBlank()) {
            flowOf(null)
        } else {
            userDao.observeByEmail(email)
        }
    }

    val allStudents: Flow<List<UserAccountEntity>> = userDao.observeAllStudents()

    suspend fun ensureInitialized() {
        database.ensureSeeded()
    }

    suspend fun signIn(
        rawEmail: String,
        rawPassword: String,
        designerModeOnly: Boolean
    ): Result<UserAccountEntity> {
        database.ensureSeeded()
        val email = rawEmail.trim().lowercase()
        val password = rawPassword.trim()

        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Masukkan alamat email yang valid."))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Kata sandi tidak boleh kosong."))
        }

        if (designerModeOnly && email != DesignerProfileData.DESIGNER_EMAIL) {
            return Result.failure(
                IllegalArgumentException("Akses perancang terkunci khusus untuk ${DesignerProfileData.DESIGNER_EMAIL}.")
            )
        }

        val user = userDao.getByEmail(email)
            ?: return Result.failure(
                IllegalArgumentException("Akun belum terdaftar. Silakan buat akun atau email baru terlebih dahulu.")
            )

        val valid = if (email == DesignerProfileData.DESIGNER_EMAIL) {
            password.equals(DesignerProfileData.DESIGNER_PASSPHRASE, ignoreCase = true)
        } else {
            user.passwordHash == SecurityUtils.hashPassword(email, password)
        }

        if (!valid) {
            return Result.failure(
                IllegalArgumentException(
                    if (email == DesignerProfileData.DESIGNER_EMAIL) {
                        "Sandi perancang salah. Gunakan sandi nama pecinta kopi."
                    } else {
                        "Kata sandi salah. Anda dapat memulihkan sandi atau membuat email baru."
                    }
                )
            )
        }

        sessionManager.saveSession(email)
        return Result.success(user)
    }

    suspend fun registerStudent(
        rawEmail: String,
        rawPassword: String,
        fullName: String,
        schoolOrCampus: String,
        originRegion: String,
        arabicLevel: String
    ): Result<UserAccountEntity> {
        database.ensureSeeded()
        val email = rawEmail.trim().lowercase()
        val password = rawPassword.trim()
        val name = fullName.trim()

        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Nama lengkap wajib diisi."))
        }
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Format email tidak valid."))
        }
        if (email == DesignerProfileData.DESIGNER_EMAIL) {
            return Result.failure(IllegalArgumentException("Email tersebut khusus milik perancang aplikasi."))
        }
        if (password.length < 4) {
            return Result.failure(IllegalArgumentException("Kata sandi minimal 4 karakter."))
        }
        if (userDao.getByEmail(email) != null) {
            return Result.failure(IllegalArgumentException("Email sudah terdaftar. Gunakan email lain atau buat email baru."))
        }

        val newUser = UserAccountEntity(
            email = email,
            passwordHash = SecurityUtils.hashPassword(email, password),
            role = "STUDENT",
            fullName = name,
            schoolOrCampus = schoolOrCampus.trim(),
            originRegion = originRegion.trim(),
            arabicLevel = arabicLevel
        )
        userDao.upsertUser(newUser)
        sessionManager.saveSession(email)
        return Result.success(newUser)
    }

    suspend fun resetStudentPassword(
        rawEmail: String,
        fullNameVerification: String,
        newPassword: String
    ): Result<Unit> {
        val email = rawEmail.trim().lowercase()
        if (email == DesignerProfileData.DESIGNER_EMAIL) {
            return Result.failure(IllegalArgumentException("Akun perancang tidak dapat diubah melalui pemulihan pelajar."))
        }
        val user = userDao.getByEmail(email)
            ?: return Result.failure(IllegalArgumentException("Email tidak ditemukan."))

        if (!user.fullName.trim().equals(fullNameVerification.trim(), ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("Nama lengkap verifikasi tidak cocok dengan data akun."))
        }
        if (newPassword.trim().length < 4) {
            return Result.failure(IllegalArgumentException("Sandi baru minimal 4 karakter."))
        }
        val updated = user.copy(passwordHash = SecurityUtils.hashPassword(email, newPassword.trim()))
        userDao.updateUser(updated)
        return Result.success(Unit)
    }

    suspend fun updateProfile(updatedUser: UserAccountEntity): Result<Unit> {
        userDao.updateUser(updatedUser)
        return Result.success(Unit)
    }

    suspend fun signOut() {
        sessionManager.clearSession()
    }
}
