package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DesignerProfileData
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal
import com.example.ui.viewmodel.AuthUiState

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onSignIn: (email: String, password: String, designerModeOnly: Boolean) -> Unit,
    onRegisterStudent: (
        email: String,
        password: String,
        fullName: String,
        school: String,
        region: String,
        level: String
    ) -> Unit,
    onResetPassword: (email: String, fullName: String, newPassword: String) -> Unit,
    onClearMessages: () -> Unit
) {
    // 0 = Pelajar, 1 = Perancang
    var portalIndex by remember { mutableIntStateOf(0) }
    // 0 = Masuk, 1 = Buat Akun / Email Baru
    var studentAuthTab by remember { mutableIntStateOf(0) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Registration fields
    var fullName by remember { mutableStateOf("") }
    var schoolOrCampus by remember { mutableStateOf("") }
    var originRegion by remember { mutableStateOf("") }
    var arabicLevel by remember { mutableStateOf("Pemula (Mubtadi')") }

    var showForgotDialog by remember { mutableStateOf(false) }

    com.example.ui.components.ArabianPatternBackground(
        darkArabianStyle = true,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_hero_arabiyyah),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .alpha(0.30f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.widthIn(max = 520.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                com.example.ui.components.GoldCalligraphyText(
                    text = "إِحْيَاءُ العَرَبِيَّةِ",
                    fontSize = 40.sp
                )
                Text(
                    text = "Iḥyā’ul ‘Arabiyyah",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFFFFF8E7),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Platform Peningkatan & Penjagaan Kemampuan Bahasa Arab",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GoldRadiant.copy(alpha = 0.92f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(18.dp))

                // Dual Role Access Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = portalIndex == 0,
                        onClick = {
                            portalIndex = 0
                            email = ""
                            password = ""
                            onClearMessages()
                        },
                        label = {
                            Text(
                                "Akses Pelajar",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.School, contentDescription = null)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldRoyal,
                            selectedLabelColor = EmeraldNight,
                            selectedLeadingIconColor = EmeraldNight,
                            containerColor = Color.White.copy(alpha = 0.1f),
                            labelColor = Color.White,
                            iconColor = GoldRadiant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("portal_student_chip")
                    )
                    FilterChip(
                        selected = portalIndex == 1,
                        onClick = {
                            portalIndex = 1
                            email = DesignerProfileData.DESIGNER_EMAIL
                            password = ""
                            onClearMessages()
                        },
                        label = {
                            Text(
                                "Akses Perancang",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = null)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldRoyal,
                            selectedLabelColor = EmeraldNight,
                            selectedLeadingIconColor = EmeraldNight,
                            containerColor = Color.White.copy(alpha = 0.1f),
                            labelColor = Color.White,
                            iconColor = GoldRadiant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("portal_designer_chip")
                    )
                }

                Spacer(Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, GoldRoyal.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (portalIndex == 0) {
                            TabRow(
                                selectedTabIndex = studentAuthTab,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                            ) {
                                Tab(
                                    selected = studentAuthTab == 0,
                                    onClick = {
                                        studentAuthTab = 0
                                        onClearMessages()
                                    },
                                    text = { Text("Masuk Email") }
                                )
                                Tab(
                                    selected = studentAuthTab == 1,
                                    onClick = {
                                        studentAuthTab = 1
                                        onClearMessages()
                                    },
                                    text = { Text("Buat Email Baru") }
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GoldRoyal.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, GoldRoyal)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Portal Khusus Perancang Aplikasi",
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDeep
                                    )
                                    Text(
                                        text = "Hanya akun ${DesignerProfileData.DESIGNER_EMAIL} yang dapat masuk untuk mengelola materi, ujian, dan sertifikat.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        if (uiState.errorMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                            ) {
                                Text(
                                    text = uiState.errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        if (uiState.infoMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = EmeraldVibrant.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, EmeraldVibrant)
                            ) {
                                Text(
                                    text = uiState.infoMessage,
                                    color = EmeraldDeep,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = portalIndex == 0 && studentAuthTab == 1) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = fullName,
                                    onValueChange = { fullName = it },
                                    label = { Text("Nama Lengkap Pelajar*") },
                                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = schoolOrCampus,
                                    onValueChange = { schoolOrCampus = it },
                                    label = { Text("Sekolah / Kampus / Pesantren") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = originRegion,
                                    onValueChange = { originRegion = it },
                                    label = { Text("Asal Daerah") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedButton(
                                    onClick = {
                                        val slug = fullName.trim().lowercase()
                                            .replace(Regex("[^a-z0-9]"), "")
                                            .ifBlank { "pelajar" }
                                        val code = (100..999).random()
                                        email = "$slug$code@ihya.arab"
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("generate_new_email_button")
                                ) {
                                    Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Buat Email Baru di Aplikasi (@ihya.arab)")
                                }
                            }
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = {
                                Text(
                                    if (portalIndex == 1) "Email Perancang"
                                    else "Alamat Email"
                                )
                            },
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            singleLine = true,
                            enabled = portalIndex == 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_input")
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = {
                                Text(
                                    if (portalIndex == 1) "Sandi Perancang (nama pecinta kopi)"
                                    else "Kata Sandi"
                                )
                            },
                            leadingIcon = { Icon(Icons.Filled.Key, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = "Tampilkan sandi"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_input")
                        )

                        if (portalIndex == 0 && studentAuthTab == 1) {
                            Text(
                                text = "Pilih Tingkatan Awal:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("Pemula (Mubtadi')", "Menengah", "Mahir").forEach { lvl ->
                                    FilterChip(
                                        selected = arabicLevel == lvl,
                                        onClick = { arabicLevel = lvl },
                                        label = { Text(lvl, fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (portalIndex == 1) {
                                    onSignIn(email, password, true)
                                } else if (studentAuthTab == 0) {
                                    onSignIn(email, password, false)
                                } else {
                                    onRegisterStudent(
                                        email,
                                        password,
                                        fullName,
                                        schoolOrCampus,
                                        originRegion,
                                        arabicLevel
                                    )
                                }
                            },
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("auth_submit_button")
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text(
                                    text = when {
                                        portalIndex == 1 -> "Masuk sebagai Perancang"
                                        studentAuthTab == 0 -> "Masuk ke Aplikasi"
                                        else -> "Buat Email & Daftar"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        if (portalIndex == 0 && studentAuthTab == 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = { showForgotDialog = true }) {
                                    Text("Lupa Sandi?")
                                }
                                TextButton(onClick = { studentAuthTab = 1 }) {
                                    Text("Email Bermasalah? Buat Baru")
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Dirancang oleh ${DesignerProfileData.NAME} • ${DesignerProfileData.UNIVERSITY} (NIM ${DesignerProfileData.NIM})",
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldRadiant.copy(alpha = 0.88f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showForgotDialog) {
        var resetEmail by remember { mutableStateOf(email) }
        var resetFullName by remember { mutableStateOf("") }
        var resetNewPass by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            title = { Text("Pulihkan Sandi Pelajar", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Masukkan email dan nama lengkap sesuai data pendaftaran, atau buat email baru langsung di tab Buat Email Baru.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Email Terdaftar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = resetFullName,
                        onValueChange = { resetFullName = it },
                        label = { Text("Nama Lengkap Terdaftar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = resetNewPass,
                        onValueChange = { resetNewPass = it },
                        label = { Text("Kata Sandi Baru (min 4 karakter)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onResetPassword(resetEmail, resetFullName, resetNewPass)
                    showForgotDialog = false
                }) {
                    Text("Simpan Sandi Baru")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}
