package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

enum class UserRole {
    STUDENT,
    DESIGNER
}

enum class LearningCategory(
    val titleId: String,
    val arabicTitle: String,
    val subtitle: String,
    val icon: ImageVector
) {
    MATERI(
        titleId = "Materi",
        arabicTitle = "المَوَادُّ الدِّرَاسِيَّةُ",
        subtitle = "Kaidah Nahwu, Shorof & Kosa Kata",
        icon = Icons.AutoMirrored.Filled.MenuBook
    ),
    VIDEO(
        titleId = "Video",
        arabicTitle = "الفِيدْيُو التَّعْلِيمِيُّ",
        subtitle = "Penjelasan Visual & Pelafalan",
        icon = Icons.Filled.PlayCircle
    ),
    CERITA(
        titleId = "Cerita",
        arabicTitle = "القِصَّةُ العَرَبِيَّةُ",
        subtitle = "Qishah Berharakat & Hikmah",
        icon = Icons.Filled.AutoStories
    ),
    MENULIS(
        titleId = "Menulis",
        arabicTitle = "الكِتَابَةُ وَالإِمْلَاءُ",
        subtitle = "Latihan Khat & Ketik Arab",
        icon = Icons.Filled.EditNote
    ),
    MENDENGAR(
        titleId = "Mendengar",
        arabicTitle = "الِاسْتِمَاعُ",
        subtitle = "Istima' & Pelafalan Suara Arab",
        icon = Icons.Filled.Headphones
    ),
    MENGANALISIS(
        titleId = "Menganalisis",
        arabicTitle = "التَّحْلِيلُ النَّحْوِيُّ",
        subtitle = "Tahlil I'rab & Kedudukan Kata",
        icon = Icons.Filled.Analytics
    ),
    MENERJEMAH(
        titleId = "Menerjemah",
        arabicTitle = "التَّرْجَمَةُ",
        subtitle = "Latihan Tarjamah Arab-Indonesia",
        icon = Icons.Filled.Translate
    ),
    UNGKAPAN(
        titleId = "Ungkapan Sehari-hari",
        arabicTitle = "التَّعْبِيرَاتُ اليَوْمِيَّةُ",
        subtitle = "Percakapan & Hiwar Harian",
        icon = Icons.Filled.ChatBubble
    ),
    POLA_KALIMAT(
        titleId = "Pola Kalimat",
        arabicTitle = "تَرَاكِيبُ الجُمَلِ",
        subtitle = "Tarkib & Penyusunan Kalimat",
        icon = Icons.Filled.AccountTree
    )
}

enum class ExamPredicate(
    val displayName: String,
    val arabicName: String,
    val minScore: Int
) {
    MUMTAZ("Mumtaz", "مُمْتَازٌ", 90),
    JAYYID_JIDDAN("Jayyid Jiddan", "جَيِّدٌ جِدًّا", 80),
    JAYYID("Jayyid", "جَيِّدٌ", 70),
    MAKBUL("Makbul", "مَقْبُولٌ", 60),
    BELUM_LULUS("Perlu Pengayaan", "يَحْتَاجُ إِلَى تَدْرِيبٍ", 0);

    companion object {
        fun fromScore(score: Int): ExamPredicate = when {
            score >= 90 -> MUMTAZ
            score >= 80 -> JAYYID_JIDDAN
            score >= 70 -> JAYYID
            score >= 60 -> MAKBUL
            else -> BELUM_LULUS
        }

        fun certificatePredicates(): List<ExamPredicate> = listOf(
            MAKBUL,
            JAYYID,
            JAYYID_JIDDAN,
            MUMTAZ
        )
    }
}

object DesignerProfileData {
    const val DESIGNER_EMAIL = "secangkir75@gmail.com"
    const val DESIGNER_PASSPHRASE = "pecinta kopi"
    const val NAME = "Ilman Alfarizi"
    const val BIRTH_PLACE_DATE = "Tentena, 06 Oktober 2005"
    const val ORIGIN_REGION = "Sulawesi Tengah"
    const val UNIVERSITY = "UIN Mataram"
    const val NIM = "240102065"
    const val PHONE = "082283514946"
}
