package com.example.ui.components

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

class ArabicSpeechPlayer(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady by mutableStateOf(false)
    var lastStatusMessage by mutableStateOf<String?>(null)
        private set

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val arLocale = Locale("ar", "SA")
                val result = tts?.setLanguage(arLocale)
                isReady = true
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.forLanguageTag("ar"))
                }
            }
        }
    }

    fun speak(text: String, slow: Boolean = false) {
        if (!isReady || tts == null) {
            lastStatusMessage = "Mesin suara sedang disiapkan, coba sebentar lagi."
            return
        }
        tts?.setSpeechRate(if (slow) 0.72f else 0.95f)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "arab_tts_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

@Composable
fun rememberArabicSpeechPlayer(): ArabicSpeechPlayer {
    val context = LocalContext.current
    val player = remember(context) { ArabicSpeechPlayer(context) }
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    return player
}
