package com.example.data.local

import com.example.data.model.DesignerProfileData
import com.example.data.model.LearningCategory
import java.security.MessageDigest

object SecurityUtils {
    fun hashPassword(email: String, rawPassword: String): String {
        val normalized = "${email.trim().lowercase()}|ihya_arabiyyah_2026|${rawPassword.trim()}"
        val bytes = MessageDigest.getInstance("SHA-256").digest(normalized.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

object SeedData {
    fun defaultDesignerAccount(): UserAccountEntity {
        val email = DesignerProfileData.DESIGNER_EMAIL
        return UserAccountEntity(
            email = email,
            passwordHash = SecurityUtils.hashPassword(email, DesignerProfileData.DESIGNER_PASSPHRASE),
            role = "DESIGNER",
            fullName = DesignerProfileData.NAME,
            schoolOrCampus = DesignerProfileData.UNIVERSITY,
            originRegion = DesignerProfileData.ORIGIN_REGION,
            arabicLevel = "Perancang & Pengampu Bahasa Arab",
            phone = DesignerProfileData.PHONE,
            learningGoal = "Membimbing pelajar mencintai dan menjaga Bahasa Arab"
        )
    }

    fun initialLearningContents(): List<LearningContentEntity> = listOf(
        // 1. MATERI
        LearningContentEntity(
            category = LearningCategory.MATERI.name,
            title = "Pengenalan Isim, Fi'il, dan Harf",
            arabicText = "الكَلِمَةُ ثَلَاثَةُ أَقْسَامٍ: اِسْمٌ وَفِعْلٌ وَحَرْفٌ",
            transliteration = "Al-kalimatu tsalātsatu aqsāmin: ismun wa fi‘lun wa ḥarfun.",
            indonesianTranslation = "Kata dalam Bahasa Arab terbagi menjadi tiga bagian: Isim (kata benda), Fi'il (kata kerja), dan Harf (kata penghubung/depan).",
            explanation = "• Isim (اِسْمٌ): Kata yang menunjukkan makna mandiri dan tidak terikat waktu, contoh: كِتَابٌ (buku), طَالِبٌ (pelajar).\n• Fi'il (فِعْلٌ): Kata kerja yang terikat waktu (lampau, sekarang/akan datang, perintah), contoh: كَتَبَ (telah menulis), يَقْرَأُ (sedang membaca).\n• Harf (حَرْفٌ): Kata yang maknanya sempurna jika bersama kata lain, contoh: فِي (di dalam), إِلَى (ke).",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.MATERI.name,
            title = "Ismul Isyarah (Kata Tunjuk)",
            arabicText = "هٰذَا كِتَابٌ جَدِيدٌ وَتِلْكَ مَكْتَبَةٌ وَاسِعَةٌ",
            transliteration = "Hādzā kitābun jadīdun wa tilka maktabatun wāsi‘atun.",
            indonesianTranslation = "Ini adalah buku baru dan itu adalah perpustakaan yang luas.",
            explanation = "Gunakan هٰذَا (hādzā) untuk muzakkar (maskulin) dekat, هٰذِهِ (hādzihī) untuk muannats (feminin) dekat. Untuk jarak jauh gunakan ذٰلِكَ (dzālika) untuk muzakkar dan تِلْكَ (tilka) untuk muannats.",
            level = "Dasar"
        ),

        // 2. VIDEO
        LearningContentEntity(
            category = LearningCategory.VIDEO.name,
            title = "Perkenalan Bahasa Arab Lengkap (Native)",
            arabicText = "السَّلَامُ عَلَيْكُمْ، كَيْفَ حَالُكَ؟ أَنَا بِخَيْرٍ وَالحَمْدُ لِلّٰهِ",
            transliteration = "As-salāmu ‘alaikum, kaifa ḥāluka? Anā bikhairin wal-ḥamdulillāh.",
            indonesianTranslation = "Assalamu'alaikum, bagaimana kabarmu? Saya baik, alhamdulillah.",
            explanation = "Simak pelafalan penutur asli (native) Bahasa Arab langsung di dalam aplikasi. Tekan tombol 'Putar Video di Aplikasi' untuk menonton tanpa keluar ke YouTube.",
            mediaUrl = "https://www.youtube.com/watch?v=sAgFhhFYaEU",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.VIDEO.name,
            title = "Hiwar Perkenalan Bahasa Arab (At-Ta'aruf)",
            arabicText = "السَّلَامُ عَلَيْكُمْ، أَنَا طَالِبٌ فِي الجَامِعَةِ الإِسْلَامِيَّةِ",
            transliteration = "As-salāmu ‘alaikum, anā thālibun fil-jāmi‘atil-islāmiyyah.",
            indonesianTranslation = "Assalamu'alaikum, saya seorang mahasiswa di universitas Islam.",
            explanation = "Video praktik percakapan perkenalan diri lengkap dengan terjemahan dan intonasi Arab fusha modern. Putar langsung di aplikasi dan catat kosakata barunya!",
            mediaUrl = "https://www.youtube.com/watch?v=QWOtau1vMqw",
            level = "Menengah"
        ),

        // 3. CERITA
        LearningContentEntity(
            category = LearningCategory.CERITA.name,
            title = "Pemuda Pencari Ilmu di Pagi Hari",
            arabicText = "اسْتَيْقَظَ زَيْدٌ مُبَكِّرًا، ثُمَّ تَوَضَّأَ وَصَلَّى الفَجْرَ. بَعْدَ ذٰلِكَ قَرَأَ القُرْآنَ وَحَفِظَ عَشَرَ كَلِمَاتٍ عَرَبِيَّةٍ قَبْلَ الذَّهَابِ إِلَى المَدْرَسَةِ.",
            transliteration = "Istaiqazha Zaidun mubakkiran, tsumma tawadhdha'a wa shallal-fajra. Ba‘da dzālika qara'al-qur'āna wa ḥafizha ‘asyara kalimātin ‘arabiyyatin qabladz-dzahābi ilal-madrasah.",
            indonesianTranslation = "Zaid bangun pagi-pagi sekali, lalu berwudhu dan menunaikan shalat Subuh. Setelah itu ia membaca Al-Qur'an dan menghafal sepuluh kosakata Arab sebelum berangkat ke sekolah.",
            explanation = "Hikmah: Menjaga kebiasaan menghafal minimal 10 mufradat setiap pagi akan menjaga lidah dan ingatan Bahasa Arab tetap hidup (Iḥyā’ul ‘Arabiyyah).",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.CERITA.name,
            title = "Cahaya Perpustakaan",
            arabicText = "دَخَلَتْ فَاطِمَةُ المَكْتَبَةَ وَبَحَثَتْ عَنْ كِتَابِ النَّحْوِ. وَجَدَتْهُ عَلَى الرَّفِّ الأَعْلَى فَقَرَأَتْهُ بِشَغَفٍ حَتَّى فَهِمَتْ قَوَاعِدَ اللُّغَةِ.",
            transliteration = "Dakhalat Fāthimatul-maktabata wa baḥatsat ‘an kitābin-naḥwi. Wajadathu ‘alar-raffil-a‘lā faqara'athu bisyaghafin ḥattā fahimat qawā‘idal-lughah.",
            indonesianTranslation = "Fatimah memasuki perpustakaan dan mencari buku Nahwu. Ia menemukannya di rak paling atas lalu membacanya dengan penuh semangat hingga memahami kaidah bahasa.",
            explanation = "Perhatikan penggunaan Fi'il Madhi ber-ta' ta'nits (دَخَلَتْ، بَحَثَتْ، قَرَأَتْ) karena subjeknya adalah muannats (فَاطِمَةُ).",
            level = "Menengah"
        ),

        // 4. MENULIS
        LearningContentEntity(
            category = LearningCategory.MENULIS.name,
            title = "Latihan Menulis Kalimat Motivasi Arab",
            arabicText = "العِلْمُ نُورٌ وَالجَهْلُ ظَلَامٌ",
            transliteration = "Al-‘ilmu nūrun wal-jahlu zhalāmun.",
            indonesianTranslation = "Ilmu adalah cahaya dan kebodohan adalah kegelapan.",
            explanation = "Gunakan kanvas goresan di bawah untuk berlatih menulis huruf ‘Ain (ع), Mim (م), Nun (ن), dan Wawu (و) dari kanan ke kiri, lalu ketik ulang kalimat tersebut di kolom latihan ketik.",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.MENULIS.name,
            title = "Imla' Kalimat Keseharian",
            arabicText = "أُحِبُّ اللُّغَةَ العَرَبِيَّةَ لِأَنَّهَا لُغَةُ القُرْآنِ",
            transliteration = "Uḥibbul-lughatal-‘arabiyyata li'annahā lughatul-qur'ān.",
            indonesianTranslation = "Aku mencintai Bahasa Arab karena ia adalah bahasa Al-Qur'an.",
            explanation = "Perhatikan penulisan hamzah qatha' pada أُحِبُّ dan ta' marbuthah (ة) pada اللُّغَةَ serta العَرَبِيَّةَ.",
            level = "Menengah"
        ),

        // 5. MENDENGAR (ISTIMA')
        LearningContentEntity(
            category = LearningCategory.MENDENGAR.name,
            title = "Istima': Dialog di Perpustakaan",
            arabicText = "السَّلَامُ عَلَيْكُمْ، أَيْنَ كُتُبُ الأَدَبِ العَرَبِيِّ؟ هِيَ فِي الجَنَاحِ الثَّانِي بِجَانِبِ القَوَامِيسِ.",
            transliteration = "As-salāmu ‘alaikum, aina kutubul-adabil-‘arabī? Hiya fil-janāḥits-tsānī bijānibil-qawāmīs.",
            indonesianTranslation = "Assalamu'alaikum, di mana buku-buku sastra Arab? Buku-buku itu berada di sayap kedua di samping kamus-kamus.",
            explanation = "Tekan tombol 'Putar Suara Arab' untuk mendengarkan pelafalan kalimat secara langsung. Perhatikan bunyi panjang (mad) pada العَرَبِيِّ dan القَوَامِيسِ.",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.MENDENGAR.name,
            title = "Istima': Menyimak Berita Singkat",
            arabicText = "اجْتَمَعَ الطُّلَّابُ فِي القَاعَةِ الكَبِيرَةِ لِحُضُورِ نَدْوَةِ إِحْيَاءِ العَرَبِيَّةِ.",
            transliteration = "Ijtama‘ath-thullābu fil-qā‘atil-kabīrati liḥudhūri nadwati iḥyā’il-‘arabiyyah.",
            indonesianTranslation = "Para mahasiswa berkumpul di aula besar untuk menghadiri seminar menghidupkan Bahasa Arab.",
            explanation = "Dengarkan bunyi idgham syamsiyah pada الطُّلَّابُ (ath-thullābu) dan huruf ق pada القَاعَةِ.",
            level = "Menengah"
        ),

        // 6. MENGANALISIS (TAHLIL)
        LearningContentEntity(
            category = LearningCategory.MENGANALISIS.name,
            title = "Analisis I'rab Jumlah Fi'liyyah",
            arabicText = "كَتَبَ الطَّالِبُ الدَّرْسَ فِي الدَّفْتَرِ",
            transliteration = "Katabath-thālibud-darsa fid-daftari.",
            indonesianTranslation = "Pelajar itu telah menulis pelajaran di dalam buku tulis.",
            explanation = "Hasil Analisis Nahwu:\n1. كَتَبَ : Fi'il Madhi mabni 'alal fathah (kata kerja lampau).\n2. الطَّالِبُ : Fa'il (subjek) marfu' dengan tanda dhammah.\n3. الدَّرْسَ : Maf'ul bih (objek) manshub dengan tanda fathah.\n4. فِي : Harf jar.\n5. الدَّفْتَرِ : Isim majrur dengan tanda kasrah karena didahului فِي.",
            level = "Menengah"
        ),
        LearningContentEntity(
            category = LearningCategory.MENGANALISIS.name,
            title = "Analisis Mubtada' & Khabar",
            arabicText = "المَدْرَسَةُ نَظِيفَةٌ وَجَمِيلَةٌ",
            transliteration = "Al-madrasatu nazhīfatun wa jamīlatun.",
            indonesianTranslation = "Sekolah itu bersih dan indah.",
            explanation = "Hasil Analisis:\n1. المَدْرَسَةُ : Mubtada' (pokok kalimat) marfu' dengan dhammah.\n2. نَظِيفَةٌ : Khabar (predikat) marfu' dengan dhammah, menyesuaikan jenis muannats dengan mubtada'.\n3. وَ : Harf 'athaf.\n4. جَمِيلَةٌ : Ma'thuf kepada نَظِيفَةٌ (sama-sama marfu').",
            level = "Dasar"
        ),

        // 7. MENERJEMAH (TARJAMAH)
        LearningContentEntity(
            category = LearningCategory.MENERJEMAH.name,
            title = "Terjemahan Teks Motivasi Belajar",
            arabicText = "مَنْ جَدَّ وَجَدَ، وَمَنْ زَرَعَ حَصَدَ، وَمَنْ صَبَرَ ظَفِرَ",
            transliteration = "Man jadda wajada, wa man zara‘a ḥashada, wa man shabara zhafira.",
            indonesianTranslation = "Barang siapa bersungguh-sungguh maka ia akan berhasil, barang siapa menanam maka ia akan menuai, dan barang siapa bersabar maka ia akan beruntung.",
            explanation = "Coba tuliskan terjemahanmu terlebih dahulu sebelum membuka kunci terjemahan perancang!",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.MENERJEMAH.name,
            title = "Terjemahan Ungkapan Akademik",
            arabicText = "تَعَلُّمُ اللُّغَةِ العَرَبِيَّةِ مِفْتَاحٌ لِفَهْمِ التُّرَاثِ الإِسْلَامِيِّ",
            transliteration = "Ta‘allumul-lughatil-‘arabiyyati miftāḥun lifahmit-turātsil-islāmī.",
            indonesianTranslation = "Mempelajari Bahasa Arab adalah kunci untuk memahami khazanah keilmuan Islam.",
            explanation = "Perhatikan idhafah (kata majemuk) pada تَعَلُّمُ اللُّغَةِ dan التُّرَاثِ الإِسْلَامِيِّ (na'at-man'ut).",
            level = "Menengah"
        ),

        // 8. UNGKAPAN SEHARI-HARI
        LearningContentEntity(
            category = LearningCategory.UNGKAPAN.name,
            title = "Menyapa & Menanyakan Kabar",
            arabicText = "صَبَاحُ الخَيْرِ! كَيْفَ حَالُكَ اليَوْمَ؟ بِخَيْرٍ وَالحَمْدُ لِلّٰهِ",
            transliteration = "Shabāḥul-khair! Kaifa ḥālukal-yaum? Bikhairin wal-ḥamdulillāh.",
            indonesianTranslation = "Selamat pagi! Bagaimana kabarmu hari ini? Baik, alhamdulillah.",
            explanation = "Digunakan setiap pagi saat bertemu teman, guru, atau keluarga. Untuk perempuan ubah كَ menjadi كِ (ḥāluki).",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.UNGKAPAN.name,
            title = "Meminta Izin & Ucapan Terima Kasih",
            arabicText = "لَوْ سَمَحْتَ، هَلْ يُمْكِنُنِي أَنْ أَسْأَلَ؟ شُكْرًا جَزِيلًا",
            transliteration = "Lau samaḥta, hal yumkinunī an as'ala? Syukran jazīlan.",
            indonesianTranslation = "Permisi, bolehkah saya bertanya? Terima kasih banyak.",
            explanation = "Ungkapan sopan santun di dalam kelas atau forum diskusi ilmiah.",
            level = "Dasar"
        ),

        // 9. POLA PENYUSUNAN KALIMAT (TARKIB)
        LearningContentEntity(
            category = LearningCategory.POLA_KALIMAT.name,
            title = "Pola 1: Jumlah Ismiyyah (Mubtada' + Khabar)",
            arabicText = "[المُبْتَدَأُ] + [الخَبَرُ] ← الطَّالِبُ مُجْتَهِدٌ",
            transliteration = "[Al-Mubtada'] + [Al-Khabar] → Ath-thālibu mujtahidun.",
            indonesianTranslation = "[Subjek Isim Ma'rifah] + [Predikat Nakirah] → Pelajar itu bersungguh-sungguh.",
            explanation = "Rumus Penyusunan:\n1. Mubtada' diawali ال (Alif-Lam) dan berharakat akhir dhammah.\n2. Khabar menyesuaikan jenis (muzakkar/muannats) dan jumlah (mufrad/mutsanna/jamak) dengan Mubtada'.",
            level = "Dasar"
        ),
        LearningContentEntity(
            category = LearningCategory.POLA_KALIMAT.name,
            title = "Pola 2: Jumlah Fi'liyyah (Fi'il + Fa'il + Maf'ul Bih)",
            arabicText = "[الفِعْلُ] + [الفَاعِلُ] + [المَفْعُولُ بِهِ] ← يَقْرَأُ أَحْمَدُ الكِتَابَ",
            transliteration = "[Al-Fi‘l] + [Al-Fā‘il] + [Al-Maf‘ūl bih] → Yaqra'u Aḥmadul-kitāba.",
            indonesianTranslation = "[Kata Kerja] + [Subjek] + [Objek] → Ahmad sedang membaca buku.",
            explanation = "Rumus Penyusunan:\n1. Kata kerja diletakkan di awal kalimat dalam bentuk tunggal (mufrad).\n2. Pelaku (Fa'il) berharakat akhir dhammah (marfu').\n3. Objek (Maf'ul bih) berharakat akhir fathah (manshub).",
            level = "Menengah"
        )
    )

    fun initialExamQuestions(): List<ExamQuestionEntity> = listOf(
        ExamQuestionEntity(
            questionText = "Apa pembagian jenis kata dalam Bahasa Arab yang benar?",
            arabicPrompt = "أَقْسَامُ الكَلِمَةِ هِيَ...",
            optionA = "Isim, Fi'il, dan Harf",
            optionB = "Mubtada', Khabar, dan Na'at",
            optionC = "Madhi, Mudhari', dan Amar",
            optionD = "Muzakkar dan Muannats",
            correctOptionIndex = 0,
            explanation = "Setiap kata (Al-Kalimah) dalam Bahasa Arab terbagi menjadi tiga: Isim, Fi'il, dan Harf."
        ),
        ExamQuestionEntity(
            questionText = "Pada kalimat di bawah, manakah yang berkedudukan sebagai Fa'il (pelaku)?",
            arabicPrompt = "كَتَبَ الطَّالِبُ الدَّرْسَ",
            optionA = "كَتَبَ",
            optionB = "الطَّالِبُ",
            optionC = "الدَّرْسَ",
            optionD = "كَتَبَ الدَّرْسَ",
            correctOptionIndex = 1,
            explanation = "الطَّالِبُ adalah Fa'il (subjek pelaku) yang berharakat dhammah."
        ),
        ExamQuestionEntity(
            questionText = "Apa arti dari ungkapan berikut?",
            arabicPrompt = "صَبَاحُ الخَيْرِ، كَيْفَ حَالُكَ؟",
            optionA = "Selamat malam, siapa namamu?",
            optionB = "Selamat datang, dari mana asalmu?",
            optionC = "Selamat pagi, bagaimana kabarmu?",
            optionD = "Sampai jumpa, semoga selamat",
            correctOptionIndex = 2,
            explanation = "صَبَاحُ الخَيْرِ berarti selamat pagi, dan كَيْفَ حَالُكَ berarti bagaimana kabarmu."
        ),
        ExamQuestionEntity(
            questionText = "Kata tunjuk yang tepat untuk kata مُدَرِّسَةٌ (guru perempuan) yang dekat adalah...",
            arabicPrompt = "..... مُدَرِّسَةٌ نَشِيطَةٌ",
            optionA = "هٰذَا",
            optionB = "ذٰلِكَ",
            optionC = "هٰذِهِ",
            optionD = "أُولٰئِكَ",
            correctOptionIndex = 2,
            explanation = "هٰذِهِ digunakan untuk isim muannats tunggal yang dekat."
        ),
        ExamQuestionEntity(
            questionText = "Harakat akhir dari Maf'ul Bih (objek) mufrad dalam susunan Jumlah Fi'liyyah adalah...",
            arabicPrompt = "يَقْرَأُ زَيْدٌ الكِتَا...",
            optionA = "Dhammah (ـُ)",
            optionB = "Kasrah (ـِ)",
            optionC = "Sukun (ـْ)",
            optionD = "Fathah (ـَ)",
            correctOptionIndex = 3,
            explanation = "Maf'ul Bih hukumnya manshub, dan tanda nashab isim mufrad adalah fathah (الكِتَابَ)."
        ),
        ExamQuestionEntity(
            questionText = "Terjemahan yang paling tepat untuk pepatah Arab berikut adalah...",
            arabicPrompt = "مَنْ جَدَّ وَجَدَ",
            optionA = "Barang siapa bersungguh-sungguh pasti berhasil",
            optionB = "Barang siapa berjalan pasti sampai",
            optionC = "Ilmu tanpa amal bagaikan pohon tak berbuah",
            optionD = "Kebersihan sebagian dari iman",
            correctOptionIndex = 0,
            explanation = "مَنْ جَدَّ وَجَدَ artinya barang siapa bersungguh-sungguh, maka ia akan mendapatkannya (berhasil)."
        )
    )
}
