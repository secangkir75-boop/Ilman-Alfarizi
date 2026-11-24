package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.CertificateEntity
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.LearningContentEntity
import com.example.data.local.UserAccountEntity
import com.example.data.model.LearningCategory
import com.example.ui.components.GoldBadge
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal

@Composable
fun HomeScreen(
    currentUser: UserAccountEntity,
    isDesigner: Boolean,
    allContents: List<LearningContentEntity>,
    studentAttempts: List<ExamAttemptEntity>,
    pendingCertificates: List<CertificateEntity>,
    onOpenCategory: (LearningCategory) -> Unit,
    onOpenExam: () -> Unit,
    onOpenDesignerInfo: () -> Unit,
    onOpenDesignerDashboard: () -> Unit,
    onPromptAcceptCertificate: (CertificateEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentCounts = allContents.groupingBy { it.category }.eachCount()
    val bestScore = studentAttempts.maxOfOrNull { it.score }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5.dp, GoldRoyal),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_arabiyyah),
                        contentDescription = "Spanduk Utama Iḥyā’ul ‘Arabiyyah",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(196.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        EmeraldNight.copy(alpha = 0.90f),
                                        EmeraldDeep.copy(alpha = 0.72f),
                                        com.example.ui.theme.ArabianBrownDeep.copy(alpha = 0.42f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        com.example.ui.components.GoldCalligraphyText(
                            text = "أَهْلًا وَسَهْلًا فِي إِحْيَاءِ العَرَبِيَّةِ",
                            fontSize = 23.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start
                        )
                        Text(
                            text = "Assalamu'alaikum, ${currentUser.fullName}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFFFFFAF0),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isDesigner) "Mode Perancang Aktif • ${currentUser.email}"
                            else "${currentUser.arabicLevel} • ${allContents.size} Materi Tersedia",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldRadiant
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (bestScore != null) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = GoldRoyal,
                                ) {
                                    Text(
                                        text = "Skor Terbaik: $bestScore",
                                        color = EmeraldNight,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                            if (isDesigner) {
                                Button(
                                    onClick = onOpenDesignerDashboard,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldRadiant,
                                        contentColor = EmeraldNight
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Panel Perancang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Pending Certificate Alert Prompt for Student
        if (pendingCertificates.isNotEmpty()) {
            item {
                val latestPending = pendingCertificates.first()
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5D4)),
                    border = BorderStroke(1.5.dp, GoldRoyal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPromptAcceptCertificate(latestPending) }
                        .testTag("pending_certificate_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(GoldRoyal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.WorkspacePremium,
                                contentDescription = null,
                                tint = EmeraldNight
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sertifikat Dikirim oleh Perancang!",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldNight
                            )
                            Text(
                                text = "Predikat ${latestPending.predicate} (Skor ${latestPending.score}). Ketuk untuk menerima sertifikat Anda.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GoldDeep
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Buka prompt terima sertifikat",
                            tint = EmeraldDeep
                        )
                    }
                }
            }
        }

        // Quick Action Row: Prompt Ujian & Data Perancang
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldVibrant),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenExam)
                        .testTag("home_exam_quick_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Quiz, contentDescription = null, tint = GoldRadiant)
                        Text(
                            text = "Ujian Evaluasi",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Raih predikat Maqbūl hingga Mumtāz",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, GoldRoyal),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenDesignerInfo)
                        .testTag("home_designer_info_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Badge, contentDescription = null, tint = GoldRoyal)
                        Text(
                            text = "Data Perancang",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Profil Ilman Alfarizi • UIN Mataram",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section Title for 9 Modules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "9 Ruang Belajar Bahasa Arab",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Konten disusun oleh Perancang Aplikasi",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                GoldBadge("9 Modul")
            }
        }

        // 2-Column Grid inside LazyColumn using chunked(2) for smooth scrolling
        val rows = LearningCategory.entries.chunked(2)
        items(rows.size) { rowIndex ->
            val pair = rows[rowIndex]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { category ->
                    val count = contentCounts[category.name] ?: 0
                    CategoryModuleCard(
                        category = category,
                        itemCount = count,
                        onClick = { onOpenCategory(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryModuleCard(
    category: LearningCategory,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("module_card_${category.name.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldVibrant.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.titleId,
                        tint = EmeraldVibrant,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = GoldRoyal.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = "$itemCount isi",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldDeep,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = category.arabicTitle,
                fontFamily = AmiriFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDeep,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = category.titleId,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = category.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
