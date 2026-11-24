package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CertificateEntity
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamQuestionEntity
import com.example.data.local.LearningContentEntity
import com.example.data.local.UserAccountEntity
import com.example.data.model.ExamPredicate
import com.example.data.model.LearningCategory
import com.example.ui.components.EmeraldBadge
import com.example.ui.components.ExamQuestionFormDialog
import com.example.ui.components.GoldBadge
import com.example.ui.components.LearningContentFormDialog
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GoldRoyal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DesignerDashboardScreen(
    allContents: List<LearningContentEntity>,
    allQuestions: List<ExamQuestionEntity>,
    allStudents: List<UserAccountEntity>,
    allAttempts: List<ExamAttemptEntity>,
    allCertificates: List<CertificateEntity>,
    onSaveContent: (LearningContentEntity) -> Unit,
    onDeleteContent: (Int) -> Unit,
    onSaveQuestion: (ExamQuestionEntity) -> Unit,
    onDeleteQuestion: (Int) -> Unit,
    onIssueCertificate: (
        studentEmail: String,
        studentName: String,
        examAttemptId: Int,
        score: Int,
        predicate: String,
        designerNote: String
    ) -> Unit,
    onDeleteCertificate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategoryFilter by remember { mutableStateOf<LearningCategory?>(null) }
    var showContentDialog by remember { mutableStateOf(false) }
    var editingContent by remember { mutableStateOf<LearningContentEntity?>(null) }

    var showQuestionDialog by remember { mutableStateOf(false) }
    var editingQuestion by remember { mutableStateOf<ExamQuestionEntity?>(null) }

    var showIssueCertDialog by remember { mutableStateOf(false) }
    var prefillStudentEmail by remember { mutableStateOf("") }
    var prefillStudentName by remember { mutableStateOf("") }
    var prefillAttemptId by remember { mutableIntStateOf(0) }
    var prefillScore by remember { mutableIntStateOf(95) }
    var prefillPredicate by remember { mutableStateOf(ExamPredicate.MUMTAZ.displayName) }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")) }

    Column(modifier = modifier.fillMaxSize()) {
        Surface(
            color = EmeraldNight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "Panel Perancang • Iḥyā’ul ‘Arabiyyah",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${allContents.size} Konten • ${allQuestions.size} Soal • ${allStudents.size} Pelajar • ${allAttempts.size} Ujian",
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldRoyal
                )
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 12.dp
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Konten 9 Modul") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Soal Ujian") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Hasil Pelajar") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Sertifikat") }
            )
        }

        when (selectedTab) {
            0 -> {
                val filteredContents = remember(allContents, selectedCategoryFilter) {
                    if (selectedCategoryFilter == null) allContents
                    else allContents.filter { it.category == selectedCategoryFilter!!.name }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Button(
                            onClick = {
                                editingContent = null
                                showContentDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("designer_add_content_button")
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Masukkan Konten Baru (9 Modul)", fontWeight = FontWeight.Bold)
                        }
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = selectedCategoryFilter == null,
                                    onClick = { selectedCategoryFilter = null },
                                    label = { Text("Semua (${allContents.size})") }
                                )
                            }
                            items(LearningCategory.entries) { cat ->
                                FilterChip(
                                    selected = selectedCategoryFilter == cat,
                                    onClick = { selectedCategoryFilter = cat },
                                    label = { Text(cat.titleId) }
                                )
                            }
                        }
                    }
                    items(filteredContents, key = { it.id }) { item ->
                        val catTitle = LearningCategory.entries.firstOrNull { it.name == item.category }?.titleId ?: item.category
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        GoldBadge(catTitle)
                                        EmeraldBadge(item.level)
                                    }
                                    Text(item.title, fontWeight = FontWeight.Bold)
                                    Text(
                                        item.arabicText,
                                        maxLines = 1,
                                        color = EmeraldDeep,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                IconButton(onClick = {
                                    editingContent = item
                                    showContentDialog = true
                                }) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = EmeraldVibrant)
                                }
                                IconButton(onClick = { onDeleteContent(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Button(
                            onClick = {
                                editingQuestion = null
                                showQuestionDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("designer_add_question_button")
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Tambah Soal Ujian Evaluasi", fontWeight = FontWeight.Bold)
                        }
                    }
                    items(allQuestions, key = { it.id }) { q ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(q.questionText, fontWeight = FontWeight.Bold)
                                    if (q.arabicPrompt.isNotBlank()) {
                                        Text(q.arabicPrompt, color = EmeraldDeep)
                                    }
                                    Text(
                                        text = "Kunci: Opsi ${'A' + q.correctOptionIndex}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = GoldDeep
                                    )
                                }
                                IconButton(onClick = {
                                    editingQuestion = q
                                    showQuestionDialog = true
                                }) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Edit soal", tint = EmeraldVibrant)
                                }
                                IconButton(onClick = { onDeleteQuestion(q.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Hapus soal", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Pemantauan Hasil Ujian & Pelajar (${allAttempts.size} Ujian)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (allAttempts.isEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Belum ada pelajar yang mengirimkan jawaban ujian. Pelajar terdaftar saat ini: ${allStudents.size}.",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(allAttempts, key = { it.id }) { attempt ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, GoldRoyal.copy(alpha = 0.6f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(attempt.studentName, fontWeight = FontWeight.Bold)
                                            Text(
                                                attempt.studentEmail,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        GoldBadge("Skor ${attempt.score} • ${attempt.predicate}")
                                    }
                                    Text(
                                        text = "Benar ${attempt.correctCount}/${attempt.totalQuestions} • ${dateFormat.format(Date(attempt.submittedAt))}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Button(
                                        onClick = {
                                            prefillStudentEmail = attempt.studentEmail
                                            prefillStudentName = attempt.studentName
                                            prefillAttemptId = attempt.id
                                            prefillScore = attempt.score
                                            prefillPredicate = if (attempt.score >= 60) attempt.predicate
                                            else ExamPredicate.MAKBUL.displayName
                                            showIssueCertDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = GoldRoyal,
                                            contentColor = EmeraldNight
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text("Kirim Sertifikat ke Pelajar Ini", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (allStudents.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Daftar Pelajar Terdaftar (${allStudents.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(allStudents, key = { it.email }) { student ->
                            Card(shape = RoundedCornerShape(14.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(student.fullName, fontWeight = FontWeight.Bold)
                                    Text("${student.email} • ${student.arabicLevel}", style = MaterialTheme.typography.bodyMedium)
                                    if (student.schoolOrCampus.isNotBlank() || student.originRegion.isNotBlank()) {
                                        Text(
                                            "${student.schoolOrCampus} (${student.originRegion})",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Button(
                            onClick = {
                                val firstStudent = allStudents.firstOrNull()
                                prefillStudentEmail = firstStudent?.email ?: ""
                                prefillStudentName = firstStudent?.fullName ?: ""
                                prefillAttemptId = 0
                                prefillScore = 95
                                prefillPredicate = ExamPredicate.MUMTAZ.displayName
                                showIssueCertDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldRoyal,
                                contentColor = EmeraldNight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("designer_issue_certificate_button")
                        ) {
                            Icon(Icons.Filled.WorkspacePremium, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Buat & Kirim Sertifikat Baru", fontWeight = FontWeight.Bold)
                        }
                    }

                    items(allCertificates, key = { it.id }) { cert ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, GoldRoyal.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("${cert.studentName} (${cert.certificateCode})", fontWeight = FontWeight.Bold)
                                    Text("${cert.studentEmail} • Nilai ${cert.score}", style = MaterialTheme.typography.bodyMedium)
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        GoldBadge(cert.predicate)
                                        EmeraldBadge(
                                            if (cert.isAcceptedByStudent) "Diterima Pelajar" else "Menunggu Diterima"
                                        )
                                    }
                                }
                                IconButton(onClick = { onDeleteCertificate(cert.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Hapus sertifikat", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showContentDialog) {
        LearningContentFormDialog(
            initialCategory = selectedCategoryFilter ?: LearningCategory.MATERI,
            editingContent = editingContent,
            onDismiss = { showContentDialog = false },
            onSave = { content ->
                onSaveContent(content)
                showContentDialog = false
            }
        )
    }

    if (showQuestionDialog) {
        ExamQuestionFormDialog(
            editingQuestion = editingQuestion,
            onDismiss = { showQuestionDialog = false },
            onSave = { question ->
                onSaveQuestion(question)
                showQuestionDialog = false
            }
        )
    }

    if (showIssueCertDialog) {
        IssueCertificateDialog(
            initialEmail = prefillStudentEmail,
            initialName = prefillStudentName,
            initialScore = prefillScore,
            initialPredicate = prefillPredicate,
            students = allStudents,
            onDismiss = { showIssueCertDialog = false },
            onIssue = { email, name, score, predicate, note ->
                onIssueCertificate(email, name, prefillAttemptId, score, predicate, note)
                showIssueCertDialog = false
            }
        )
    }
}

@Composable
private fun IssueCertificateDialog(
    initialEmail: String,
    initialName: String,
    initialScore: Int,
    initialPredicate: String,
    students: List<UserAccountEntity>,
    onDismiss: () -> Unit,
    onIssue: (email: String, name: String, score: Int, predicate: String, note: String) -> Unit
) {
    var studentEmail by remember { mutableStateOf(initialEmail) }
    var studentName by remember { mutableStateOf(initialName) }
    var scoreText by remember { mutableStateOf(initialScore.toString()) }
    var selectedPredicate by remember { mutableStateOf(initialPredicate) }
    var designerNote by remember { mutableStateOf("Barakallahu fiikum, teruslah menjaga kemampuan Bahasa Arab.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kirim Sertifikat ke Pelajar", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (students.isNotEmpty()) {
                    Text("Pilih Pelajar Terdaftar:", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(students) { st ->
                            FilterChip(
                                selected = studentEmail == st.email,
                                onClick = {
                                    studentEmail = st.email
                                    studentName = st.fullName
                                },
                                label = { Text(st.fullName, fontSize = 11.sp) }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = studentEmail,
                    onValueChange = { studentEmail = it },
                    label = { Text("Email Pelajar*") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Nama Penerima di Sertifikat*") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = scoreText,
                    onValueChange = { scoreText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Skor Ujian (0-100)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Predikat Sertifikat (Maqbūl – Mumtāz):", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ExamPredicate.certificatePredicates()) { pred ->
                        FilterChip(
                            selected = selectedPredicate == pred.displayName,
                            onClick = { selectedPredicate = pred.displayName },
                            label = { Text(pred.displayName, fontSize = 12.sp) }
                        )
                    }
                }
                OutlinedTextField(
                    value = designerNote,
                    onValueChange = { designerNote = it },
                    label = { Text("Pesan / Doa Perancang") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = scoreText.toIntOrNull()?.coerceIn(0, 100) ?: 85
                    onIssue(studentEmail, studentName, score, selectedPredicate, designerNote)
                },
                enabled = studentEmail.isNotBlank() && studentName.isNotBlank()
            ) {
                Text("Kirim Sertifikat")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
