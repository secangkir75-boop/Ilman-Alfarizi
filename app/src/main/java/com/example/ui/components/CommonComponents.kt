package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.CertificateEntity
import com.example.data.local.ExamQuestionEntity
import com.example.data.local.LearningContentEntity
import com.example.data.model.DesignerProfileData
import com.example.data.model.LearningCategory
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.ArabianBrownDeep
import com.example.ui.theme.ArabianBrownLight
import com.example.ui.theme.ArabianBrownMedium
import com.example.ui.theme.ArabianSandBg
import com.example.ui.theme.ArabianWarmSand
import com.example.ui.theme.ArefRuqaaFontFamily
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun emeraldGoldHeaderBrush(): Brush = Brush.linearGradient(
    colors = listOf(EmeraldNight, EmeraldDeep, EmeraldVibrant)
)

fun goldShimmerBrush(): Brush = Brush.linearGradient(
    colors = listOf(GoldDeep, GoldRoyal, GoldRadiant)
)

/**
 * Latar belakang bernuansa Arab: cokelat pasir hangat dengan grafik pola bintang delapan (Rub el Hizb) & kisi arabesque emas.
 */
@Composable
fun ArabianPatternBackground(
    modifier: Modifier = Modifier,
    darkArabianStyle: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (darkArabianStyle) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            EmeraldNight,
                            Color(0xFF1B5B3B),
                            ArabianBrownDeep
                        )
                    )
                )
            } else {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ArabianSandBg,
                            ArabianWarmSand.copy(alpha = 0.72f),
                            ArabianSandBg
                        )
                    )
                )
            }

            val patternColor = if (darkArabianStyle) {
                GoldRadiant.copy(alpha = 0.13f)
            } else {
                ArabianBrownMedium.copy(alpha = 0.10f)
            }
            val accentGold = if (darkArabianStyle) {
                GoldRoyal.copy(alpha = 0.16f)
            } else {
                GoldRoyal.copy(alpha = 0.16f)
            }

            val step = 110.dp.toPx()
            val starSize = 36.dp.toPx()
            val cols = (size.width / step).toInt() + 2
            val rows = (size.height / step).toInt() + 2

            for (row in 0..rows) {
                for (col in 0..cols) {
                    val cx = col * step + if (row % 2 == 1) step / 2f else 0f
                    val cy = row * step
                    val topLeft = Offset(cx - starSize / 2f, cy - starSize / 2f)
                    val rectSize = Size(starSize, starSize)

                    // Rub el Hizb (8-pointed Arabian star formed by two overlapping squares)
                    drawRect(
                        color = patternColor,
                        topLeft = topLeft,
                        size = rectSize,
                        style = Stroke(width = 1.4.dp.toPx())
                    )
                    rotate(degrees = 45f, pivot = Offset(cx, cy)) {
                        drawRect(
                            color = accentGold,
                            topLeft = topLeft,
                            size = rectSize,
                            style = Stroke(width = 1.4.dp.toPx())
                        )
                    }
                    drawCircle(
                        color = accentGold,
                        radius = 4.dp.toPx(),
                        center = Offset(cx, cy)
                    )
                }
            }
        }
        content()
    }
}

@Composable
fun GoldCalligraphyText(
    text: String,
    fontSize: TextUnit = 26.sp,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = ArefRuqaaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            brush = Brush.linearGradient(
                colors = listOf(GoldRadiant, GoldRoyal, Color(0xFFFFE799))
            )
        ),
        textAlign = textAlign,
        modifier = modifier
    )
}

@Composable
fun ArabicDisplayBox(
    arabicText: String,
    transliteration: String = "",
    onSpeakClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = EmeraldDeep,
        border = BorderStroke(1.4.dp, GoldRoyal)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle Arabian geometric arch accent in background
            Canvas(modifier = Modifier.matchParentSize()) {
                val archStroke = Stroke(width = 1.2.dp.toPx())
                val path = Path().apply {
                    moveTo(size.width * 0.08f, size.height)
                    lineTo(size.width * 0.08f, size.height * 0.25f)
                    quadraticTo(size.width * 0.5f, -size.height * 0.05f, size.width * 0.92f, size.height * 0.25f)
                    lineTo(size.width * 0.92f, size.height)
                }
                drawPath(path = path, color = GoldRadiant.copy(alpha = 0.12f), style = archStroke)
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "النَّصُّ العَرَبِيُّ",
                        color = GoldRadiant,
                        fontFamily = ArefRuqaaFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (onSpeakClick != null) {
                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldRoyal.copy(alpha = 0.24f))
                                .testTag("speak_arabic_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Putar pelafalan Bahasa Arab",
                                tint = GoldRadiant
                            )
                        }
                    }
                }
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = arabicText,
                        fontFamily = AmiriFontFamily,
                        fontSize = 23.sp,
                        lineHeight = 38.sp,
                        color = Color(0xFFFFFBF0),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (transliteration.isNotBlank()) {
                    HorizontalDivider(color = GoldRoyal.copy(alpha = 0.35f))
                    Text(
                        text = transliteration,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = GoldRadiant
                    )
                }
            }
        }
    }
}

@Composable
fun GoldBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = GoldRoyal.copy(alpha = 0.22f),
        border = BorderStroke(1.dp, GoldRoyal)
    ) {
        Text(
            text = text,
            color = ArabianBrownDeep,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EmeraldBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = EmeraldGlow.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, EmeraldVibrant)
    ) {
        Text(
            text = text,
            color = EmeraldNight,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningContentFormDialog(
    initialCategory: LearningCategory,
    editingContent: LearningContentEntity? = null,
    onDismiss: () -> Unit,
    onSave: (LearningContentEntity) -> Unit
) {
    var selectedCategory by remember {
        mutableStateOf(
            editingContent?.let {
                LearningCategory.entries.firstOrNull { cat -> cat.name == it.category }
            } ?: initialCategory
        )
    }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(editingContent?.title ?: "") }
    var arabicText by remember { mutableStateOf(editingContent?.arabicText ?: "") }
    var transliteration by remember { mutableStateOf(editingContent?.transliteration ?: "") }
    var translation by remember { mutableStateOf(editingContent?.indonesianTranslation ?: "") }
    var explanation by remember { mutableStateOf(editingContent?.explanation ?: "") }
    var mediaUrl by remember { mutableStateOf(editingContent?.mediaUrl ?: "") }
    var level by remember { mutableStateOf(editingContent?.level ?: "Dasar") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editingContent == null) "Tambah Konten Pembelajaran" else "Edit Konten Pembelajaran",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.titleId,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori Modul") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        LearningCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text("${category.titleId} (${category.arabicTitle})") },
                                onClick = {
                                    selectedCategory = category
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Materi / Pelajaran*") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = arabicText,
                    onValueChange = { arabicText = it },
                    label = { Text("Teks Bahasa Arab*") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    label = { Text("Transliterasi Latin (opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = translation,
                    onValueChange = { translation = it },
                    label = { Text("Arti / Terjemahan Indonesia*") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Penjelasan / Rumus / Analisis") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mediaUrl,
                    onValueChange = { mediaUrl = it },
                    label = { Text("Link Video YouTube / Audio (diputar di aplikasi)") },
                    placeholder = { Text("Contoh: https://youtu.be/... atau youtube.com/watch?v=...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Dasar", "Menengah", "Mahir").forEach { lvl ->
                        OutlinedButton(
                            onClick = { level = lvl },
                            colors = if (level == lvl) {
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = EmeraldVibrant.copy(alpha = 0.16f),
                                    contentColor = EmeraldDeep
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text(lvl, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && arabicText.isNotBlank() && translation.isNotBlank()) {
                        onSave(
                            LearningContentEntity(
                                id = editingContent?.id ?: 0,
                                category = selectedCategory.name,
                                title = title.trim(),
                                arabicText = arabicText.trim(),
                                transliteration = transliteration.trim(),
                                indonesianTranslation = translation.trim(),
                                explanation = explanation.trim(),
                                mediaUrl = mediaUrl.trim(),
                                level = level
                            )
                        )
                    }
                },
                enabled = title.isNotBlank() && arabicText.isNotBlank() && translation.isNotBlank()
            ) {
                Text("Simpan Konten")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun ExamQuestionFormDialog(
    editingQuestion: ExamQuestionEntity? = null,
    onDismiss: () -> Unit,
    onSave: (ExamQuestionEntity) -> Unit
) {
    var questionText by remember { mutableStateOf(editingQuestion?.questionText ?: "") }
    var arabicPrompt by remember { mutableStateOf(editingQuestion?.arabicPrompt ?: "") }
    var optionA by remember { mutableStateOf(editingQuestion?.optionA ?: "") }
    var optionB by remember { mutableStateOf(editingQuestion?.optionB ?: "") }
    var optionC by remember { mutableStateOf(editingQuestion?.optionC ?: "") }
    var optionD by remember { mutableStateOf(editingQuestion?.optionD ?: "") }
    var correctIndex by remember { mutableIntStateOf(editingQuestion?.correctOptionIndex ?: 0) }
    var explanation by remember { mutableStateOf(editingQuestion?.explanation ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editingQuestion == null) "Tambah Soal Ujian" else "Edit Soal Ujian",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    label = { Text("Pertanyaan (Indonesia)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = arabicPrompt,
                    onValueChange = { arabicPrompt = it },
                    label = { Text("Teks Soal Bahasa Arab (opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                listOf(
                    "Opsi A" to Pair(optionA) { v: String -> optionA = v },
                    "Opsi B" to Pair(optionB) { v: String -> optionB = v },
                    "Opsi C" to Pair(optionC) { v: String -> optionC = v },
                    "Opsi D" to Pair(optionD) { v: String -> optionD = v }
                ).forEachIndexed { idx, (label, pair) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = correctIndex == idx,
                            onClick = { correctIndex = idx }
                        )
                        OutlinedTextField(
                            value = pair.first,
                            onValueChange = pair.second,
                            label = { Text("$label*" + if (correctIndex == idx) " (Benar)" else "") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Pembahasan Jawaban") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            val valid = questionText.isNotBlank() && optionA.isNotBlank() &&
                optionB.isNotBlank() && optionC.isNotBlank() && optionD.isNotBlank()
            Button(
                onClick = {
                    onSave(
                        ExamQuestionEntity(
                            id = editingQuestion?.id ?: 0,
                            questionText = questionText.trim(),
                            arabicPrompt = arabicPrompt.trim(),
                            optionA = optionA.trim(),
                            optionB = optionB.trim(),
                            optionC = optionC.trim(),
                            optionD = optionD.trim(),
                            correctOptionIndex = correctIndex,
                            explanation = explanation.trim()
                        )
                    )
                },
                enabled = valid
            ) {
                Text("Simpan Soal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun CertificateAcceptPromptDialog(
    certificate: CertificateEntity,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.WorkspacePremium,
                contentDescription = null,
                tint = GoldRoyal,
                modifier = Modifier.size(44.dp)
            )
        },
        title = {
            Text(
                text = "Sertifikat Baru dari Perancang!",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Selamat ${certificate.studentName}! Perancang (${certificate.issuedBy}) telah menerbitkan sertifikat kelulusan untuk Anda.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldSoftSurfacePreview,
                    border = BorderStroke(1.dp, GoldRoyal)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Predikat: ${certificate.predicate}",
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Nilai Evaluasi: ${certificate.score} • Kode: ${certificate.certificateCode}",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldDeep
                        )
                        if (certificate.designerNote.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "\"${certificate.designerNote}\"",
                                fontStyle = FontStyle.Italic,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = ArabianBrownDeep
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                modifier = Modifier.testTag("accept_certificate_prompt_button")
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Terima Sertifikat")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Nanti")
            }
        }
    )
}

private val GoldSoftSurfacePreview = Color(0xFFFFF6D6)

@Composable
fun DigitalCertificateDialog(
    certificate: CertificateEntity,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")) }
    val formattedDate = remember(certificate.issuedAt) {
        dateFormat.format(Date(certificate.issuedAt))
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldNight),
            border = BorderStroke(3.dp, goldShimmerBrush()),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(1.2.dp, GoldRadiant.copy(alpha = 0.65f), RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = certificate.certificateCode,
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldRadiant.copy(alpha = 0.9f)
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Tutup sertifikat",
                                tint = GoldRadiant
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.img_certificate_seal),
                        contentDescription = "Stempel Emas Sertifikat Iḥyā’ul ‘Arabiyyah",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(2.dp, GoldRadiant, CircleShape)
                    )

                    GoldCalligraphyText(
                        text = "شَهَادَةُ التَّقْدِيرِ",
                        fontSize = 26.sp
                    )
                    Text(
                        text = "SERTIFIKAT PENGHARGAAN",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFFF8E7),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Akademi Bahasa Arab Iḥyā’ul ‘Arabiyyah",
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldRadiant
                    )

                    HorizontalDivider(color = GoldRoyal.copy(alpha = 0.45f))

                    Text(
                        text = "Diberikan dengan bangga kepada:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFFF8E7).copy(alpha = 0.9f)
                    )
                    Text(
                        text = certificate.studentName,
                        fontFamily = AmiriFontFamily,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldRadiant,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ArabianBrownDeep,
                        border = BorderStroke(1.2.dp, GoldRoyal)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Predikat Kelulusan",
                                style = MaterialTheme.typography.labelMedium,
                                color = ArabianBrownLight
                            )
                            Text(
                                text = certificate.predicate.uppercase(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldRadiant
                            )
                            Text(
                                text = "Skor Evaluasi: ${certificate.score} / 100",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFFF8E7)
                            )
                        }
                    }

                    if (certificate.designerNote.isNotBlank()) {
                        Text(
                            text = "\"${certificate.designerNote}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFFF8E7).copy(alpha = 0.92f),
                            textAlign = TextAlign.Center
                        )
                    }

                    HorizontalDivider(color = GoldRoyal.copy(alpha = 0.35f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Diterbitkan: $formattedDate",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFFF8E7).copy(alpha = 0.8f)
                            )
                            Text(
                                text = "${DesignerProfileData.UNIVERSITY} • NIM ${DesignerProfileData.NIM}",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldRadiant.copy(alpha = 0.88f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Perancang Aplikasi",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFFF8E7).copy(alpha = 0.8f)
                            )
                            Text(
                                text = certificate.issuedBy,
                                fontWeight = FontWeight.Bold,
                                color = GoldRadiant,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
