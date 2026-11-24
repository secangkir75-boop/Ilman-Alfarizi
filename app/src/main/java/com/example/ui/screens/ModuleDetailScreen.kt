package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LearningContentEntity
import com.example.data.model.LearningCategory
import com.example.ui.components.ArabicDisplayBox
import com.example.ui.components.EmeraldBadge
import com.example.ui.components.GoldBadge
import com.example.ui.components.InAppVideoPlayerCard
import com.example.ui.components.LearningContentFormDialog
import com.example.ui.components.rememberArabicSpeechPlayer
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal

private data class DrawStroke(
    val points: List<Offset>,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleDetailScreen(
    category: LearningCategory,
    contents: List<LearningContentEntity>,
    isDesigner: Boolean,
    studentEmail: String,
    onBack: () -> Unit,
    onSaveContent: (LearningContentEntity) -> Unit,
    onDeleteContent: (Int) -> Unit,
    onSavePractice: (contentId: Int, responseText: String) -> Unit
) {
    val speechPlayer = rememberArabicSpeechPlayer()

    var searchQuery by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("Semua") }
    var showFormDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<LearningContentEntity?>(null) }
    var activePlayingItemId by remember { mutableStateOf<Int?>(null) }

    val filtered = remember(contents, searchQuery, selectedLevel) {
        contents.filter { item ->
            val matchesLevel = selectedLevel == "Semua" || item.level.equals(selectedLevel, ignoreCase = true)
            val matchesQuery = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.indonesianTranslation.contains(searchQuery, ignoreCase = true) ||
                item.arabicText.contains(searchQuery, ignoreCase = true)
            matchesLevel && matchesQuery
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = category.titleId,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = category.arabicTitle,
                            fontFamily = com.example.ui.theme.ArefRuqaaFontFamily,
                            fontSize = 15.sp,
                            color = GoldRadiant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("module_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldNight,
                    titleContentColor = Color.White,
                    navigationIconContentColor = GoldRadiant
                )
            )
        },
        floatingActionButton = {
            if (isDesigner) {
                FloatingActionButton(
                    onClick = {
                        editingItem = null
                        showFormDialog = true
                    },
                    containerColor = GoldRoyal,
                    contentColor = EmeraldNight,
                    modifier = Modifier.testTag("add_module_content_fab")
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Tambah materi baru")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search and Level filter
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari ${category.titleId.lowercase()}...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Semua", "Dasar", "Menengah", "Mahir").forEach { lvl ->
                        FilterChip(
                            selected = selectedLevel == lvl,
                            onClick = { selectedLevel = lvl },
                            label = { Text(lvl, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Interactive Calligraphy Drawing Canvas for MENULIS module
            if (category == LearningCategory.MENULIS) {
                item {
                    ArabicWritingCanvasCard()
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = null,
                                tint = EmeraldVibrant,
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = "Belum ada konten ${category.titleId}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDesigner) "Tekan tombol + di kanan bawah untuk menambahkan materi."
                                else "Perancang belum menambahkan konten tambahan untuk filter ini.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filtered, key = { it.id }) { item ->
                    ModuleItemCard(
                        category = category,
                        item = item,
                        isDesigner = isDesigner,
                        studentEmail = studentEmail,
                        isVideoPlaying = activePlayingItemId == item.id,
                        onSpeak = { slow -> speechPlayer.speak(item.arabicText, slow) },
                        onToggleVideoPlay = {
                            activePlayingItemId = if (activePlayingItemId == item.id) null else item.id
                        },
                        onEdit = {
                            editingItem = item
                            showFormDialog = true
                        },
                        onDelete = {
                            if (activePlayingItemId == item.id) activePlayingItemId = null
                            onDeleteContent(item.id)
                        },
                        onSavePractice = { text -> onSavePractice(item.id, text) }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        LearningContentFormDialog(
            initialCategory = category,
            editingContent = editingItem,
            onDismiss = { showFormDialog = false },
            onSave = { content ->
                onSaveContent(content)
                showFormDialog = false
            }
        )
    }
}

@Composable
private fun ArabicWritingCanvasCard() {
    val strokes = remember { mutableStateListOf<DrawStroke>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var activePenColor by remember { mutableStateOf(GoldRadiant) }

    Card(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, GoldRoyal),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Kanvas Latihan Menulis Arab",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Tulis huruf hijaiyah dari kanan ke kiri",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = {
                        if (strokes.isNotEmpty()) strokes.removeAt(strokes.lastIndex)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Urungkan goresan")
                    }
                    IconButton(onClick = { strokes.clear() }) {
                        Icon(Icons.Filled.DeleteSweep, contentDescription = "Bersihkan kanvas")
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = activePenColor == GoldRadiant,
                    onClick = { activePenColor = GoldRadiant },
                    label = { Text("Tinta Emas") }
                )
                FilterChip(
                    selected = activePenColor == Color(0xFF00E676),
                    onClick = { activePenColor = Color(0xFF00E676) },
                    label = { Text("Tinta Hijau") }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(EmeraldDeep)
                    .border(1.dp, GoldRoyal.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(activePenColor) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPoints = currentPoints + change.position
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        strokes.add(DrawStroke(currentPoints, activePenColor))
                                        currentPoints = emptyList()
                                    }
                                }
                            )
                        }
                ) {
                    // Subtle calligraphy baseline guides
                    val yMid = size.height * 0.55f
                    drawLine(
                        color = GoldRoyal.copy(alpha = 0.22f),
                        start = Offset(0f, yMid),
                        end = Offset(size.width, yMid),
                        strokeWidth = 2f
                    )

                    fun drawStrokePath(points: List<Offset>, color: Color) {
                        if (points.size < 2) return
                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = color,
                            style = Stroke(width = 9f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }

                    strokes.forEach { drawStrokePath(it.points, it.color) }
                    drawStrokePath(currentPoints, activePenColor)
                }
            }
        }
    }
}

@Composable
private fun ModuleItemCard(
    category: LearningCategory,
    item: LearningContentEntity,
    isDesigner: Boolean,
    studentEmail: String,
    isVideoPlaying: Boolean,
    onSpeak: (slow: Boolean) -> Unit,
    onToggleVideoPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSavePractice: (String) -> Unit
) {
    var revealed by remember(item.id) {
        mutableStateOf(category != LearningCategory.MENGANALISIS && category != LearningCategory.MENERJEMAH)
    }
    var practiceText by remember(item.id, studentEmail) { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EmeraldBadge(item.level)
                    GoldBadge(category.titleId)
                }
                if (isDesigner) {
                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit materi", tint = EmeraldVibrant)
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Filled.Delete, contentDescription = "Hapus materi", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            ArabicDisplayBox(
                arabicText = item.arabicText,
                transliteration = item.transliteration,
                onSpeakClick = { onSpeak(false) }
            )

            // Special controls for MENDENGAR (Istima')
            if (category == LearningCategory.MENDENGAR || category == LearningCategory.UNGKAPAN) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSpeak(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Putar Normal", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { onSpeak(true) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.SlowMotionVideo, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Putar Lambat", fontSize = 12.sp)
                    }
                }
            }

            // Inline In-App Video / Media Player (never exits to external YouTube app)
            if (item.mediaUrl.isNotBlank()) {
                if (isVideoPlaying) {
                    InAppVideoPlayerCard(
                        title = item.title,
                        mediaUrl = item.mediaUrl,
                        onClose = onToggleVideoPlay
                    )
                } else {
                    Button(
                        onClick = onToggleVideoPlay,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldRoyal,
                            contentColor = EmeraldNight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("play_in_app_video_${item.id}")
                    ) {
                        Icon(Icons.Filled.PlayCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (category == LearningCategory.VIDEO) "Putar Video di Aplikasi"
                            else "Putar Media di Aplikasi",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Student input practice for MENULIS or MENERJEMAH
            if (category == LearningCategory.MENULIS || category == LearningCategory.MENERJEMAH) {
                OutlinedTextField(
                    value = practiceText,
                    onValueChange = { practiceText = it },
                    label = {
                        Text(
                            if (category == LearningCategory.MENULIS) "Ketik ulang kalimat Arab di atas"
                            else "Tulis percobaan terjemahanmu di sini"
                        )
                    },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    onClick = {
                        if (practiceText.isNotBlank()) onSavePractice(practiceText)
                    },
                    enabled = practiceText.isNotBlank()
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Simpan Jawaban Latihan")
                }
            }

            // Toggleable translation/analysis for interactive study
            if (category == LearningCategory.MENGANALISIS || category == LearningCategory.MENERJEMAH) {
                OutlinedButton(
                    onClick = { revealed = !revealed },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (revealed) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (revealed) "Sembunyikan Kunci & Analisis"
                        else "Lihat Terjemahan & Analisis Perancang"
                    )
                }
            }

            AnimatedVisibility(visible = revealed) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Arti / Terjemahan:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDeep
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = item.indonesianTranslation,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    if (item.explanation.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GoldRoyal.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, GoldRoyal.copy(alpha = 0.45f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (category == LearningCategory.POLA_KALIMAT) "Pola & Rumus:" else "Keterangan & Kaidah:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = item.explanation,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
