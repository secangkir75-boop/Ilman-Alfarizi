package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamQuestionEntity
import com.example.data.local.UserAccountEntity
import com.example.data.model.ExamPredicate
import com.example.ui.components.ArabicDisplayBox
import com.example.ui.components.EmeraldBadge
import com.example.ui.components.GoldBadge
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExamScreen(
    currentUser: UserAccountEntity,
    questions: List<ExamQuestionEntity>,
    attempts: List<ExamAttemptEntity>,
    lastExamAttempt: ExamAttemptEntity?,
    onSubmitExam: (questions: List<ExamQuestionEntity>, selectedAnswers: Map<Int, Int>) -> Unit,
    onDismissResultDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inExamSession by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")) }

    if (inExamSession) {
        BackHandler {
            inExamSession = false
        }
    }

    if (inExamSession && questions.isNotEmpty()) {
        val safeIndex = currentIndex.coerceIn(0, questions.lastIndex)
        val question = questions[safeIndex]
        val options = listOf(question.optionA, question.optionB, question.optionC, question.optionD)
        val progress = (safeIndex + 1).toFloat() / questions.size.toFloat()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EmeraldBadge("Soal ${safeIndex + 1} dari ${questions.size}")
                OutlinedButton(onClick = { inExamSession = false }) {
                    Text("Keluar Ujian")
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                color = EmeraldVibrant,
                trackColor = GoldRoyal.copy(alpha = 0.25f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.2.dp, GoldRoyal),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = question.questionText,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            if (question.arabicPrompt.isNotBlank()) {
                                ArabicDisplayBox(arabicText = question.arabicPrompt)
                            }
                        }
                    }
                }

                items(options.size) { idx ->
                    val isSelected = selectedAnswers[question.id] == idx
                    val letter = ('A' + idx)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) EmeraldVibrant.copy(alpha = 0.14f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) EmeraldVibrant else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedAnswers[question.id] = idx }
                            .testTag("exam_option_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedAnswers[question.id] = idx }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "$letter. ${options[idx]}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { if (safeIndex > 0) currentIndex-- },
                    enabled = safeIndex > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sebelumnya")
                }

                if (safeIndex < questions.lastIndex) {
                    Button(
                        onClick = { currentIndex++ },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Berikutnya")
                    }
                } else {
                    Button(
                        onClick = {
                            onSubmitExam(questions, selectedAnswers.toMap())
                            inExamSession = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldRoyal,
                            contentColor = EmeraldNight
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_exam_button")
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Kirim Jawaban", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldNight),
                    border = BorderStroke(1.5.dp, GoldRoyal)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Filled.Quiz,
                                contentDescription = null,
                                tint = GoldRadiant,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = "Prompt Ujian Evaluasi",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${questions.size} Soal Pilihan Ganda Bahasa Arab",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = GoldRadiant
                                )
                            }
                        }

                        Text(
                            text = "Uji pemahaman materi dari 9 ruang belajar. Hasil ujian Anda langsung tercatat agar Perancang dapat menerbitkan sertifikat predikat Maqbūl hingga Mumtāz.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.88f)
                        )

                        Button(
                            onClick = {
                                selectedAnswers.clear()
                                currentIndex = 0
                                inExamSession = true
                            },
                            enabled = questions.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldRadiant,
                                contentColor = EmeraldNight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("start_exam_button")
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (questions.isEmpty()) "Belum Ada Soal Ujian" else "Mulai Prompt Ujian Sekarang",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Predicate Legend
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Standar Predikat Kelulusan Sertifikat",
                            fontWeight = FontWeight.Bold
                        )
                        ExamPredicate.certificatePredicates().reversed().forEach { pred ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${pred.displayName} (${pred.arabicName})", fontWeight = FontWeight.Medium)
                                Text(
                                    text = "Nilai ≥ ${pred.minScore}",
                                    color = EmeraldDeep,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Riwayat Evaluasi ${currentUser.fullName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (attempts.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Anda belum mengikuti ujian. Tekan 'Mulai Prompt Ujian Sekarang' untuk mendapatkan nilai pertama Anda!",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(attempts, key = { it.id }) { attempt ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, GoldRoyal.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Predikat: ${attempt.predicate}",
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                                Text(
                                    text = "Benar ${attempt.correctCount}/${attempt.totalQuestions} soal",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = dateFormat.format(Date(attempt.submittedAt)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            GoldBadge("Skor ${attempt.score}")
                        }
                    }
                }
            }
        }
    }

    if (lastExamAttempt != null) {
        AlertDialog(
            onDismissRequest = onDismissResultDialog,
            icon = {
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = GoldRoyal,
                    modifier = Modifier.size(44.dp)
                )
            },
            title = {
                Text("Hasil Ujian Evaluasi", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${lastExamAttempt.score}",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldVibrant
                    )
                    Text(
                        text = "Predikat: ${lastExamAttempt.predicate}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = GoldDeep
                    )
                    Text(
                        text = "Jawaban benar: ${lastExamAttempt.correctCount} dari ${lastExamAttempt.totalQuestions} soal.\nHasil Anda telah dikirim ke dashboard Perancang untuk penerbitan sertifikat.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(onClick = onDismissResultDialog) {
                    Text("Alhamdulillah")
                }
            }
        )
    }
}
