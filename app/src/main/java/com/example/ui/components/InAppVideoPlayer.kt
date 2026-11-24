package com.example.ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal

object VideoUrlParser {
    private val youtubePatterns = listOf(
        Regex("(?:youtube\\.com/watch\\?(?:.*&)?v=|youtu\\.be/|youtube\\.com/embed/|youtube\\.com/shorts/|youtube\\.com/live/)([A-Za-z0-9_-]{11})")
    )

    fun extractYouTubeId(url: String): String? {
        val trimmed = url.trim()
        if (trimmed.matches(Regex("^[A-Za-z0-9_-]{11}$"))) {
            return trimmed
        }
        for (pattern in youtubePatterns) {
            val match = pattern.find(trimmed)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return null
    }

    fun isDirectVideoFile(url: String): Boolean {
        val lower = url.trim().lowercase()
        return lower.endsWith(".mp4") || lower.endsWith(".webm") || lower.endsWith(".m3u8")
    }
}

@Composable
fun InAppVideoPlayerCard(
    title: String,
    mediaUrl: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullscreenOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = EmeraldNight,
        border = BorderStroke(1.5.dp, GoldRoyal)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayCircle,
                        contentDescription = null,
                        tint = GoldRadiant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = title,
                        color = GoldRadiant,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { fullscreenOpen = true },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fullscreen,
                            contentDescription = "Perbesar pemutar video",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Tutup pemutar video",
                            tint = GoldRadiant
                        )
                    }
                }
            }

            if (!fullscreenOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                ) {
                    EmbeddedVideoWebView(
                        mediaUrl = mediaUrl,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    if (fullscreenOpen) {
        Dialog(
            onDismissRequest = { fullscreenOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                color = EmeraldNight,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            color = GoldRadiant,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { fullscreenOpen = false }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Keluar layar penuh",
                                tint = GoldRadiant
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        EmbeddedVideoWebView(
                            mediaUrl = mediaUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun EmbeddedVideoWebView(
    mediaUrl: String,
    modifier: Modifier = Modifier
) {
    val youtubeId = remember(mediaUrl) { VideoUrlParser.extractYouTubeId(mediaUrl) }
    val isDirectVideo = remember(mediaUrl) { VideoUrlParser.isDirectVideoFile(mediaUrl) }

    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    DisposableEffect(mediaUrl) {
        onDispose {
            webViewRef?.apply {
                stopLoading()
                loadUrl("about:blank")
                destroy()
            }
            webViewRef = null
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(android.graphics.Color.BLACK)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false
                    displayZoomControls = false
                    cacheMode = WebSettings.LOAD_DEFAULT
                    userAgentString =
                        "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
                }
                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val targetUrl = request?.url?.toString().orEmpty()
                        // Keep everything inside the app; block external YouTube app deep-links
                        if (targetUrl.startsWith("intent://") ||
                            targetUrl.startsWith("vnd.youtube:") ||
                            targetUrl.startsWith("youtube://")
                        ) {
                            return true
                        }
                        if (targetUrl.contains("youtube.com/watch") && !targetUrl.contains("/embed/")) {
                            val newId = VideoUrlParser.extractYouTubeId(targetUrl)
                            if (newId != null) {
                                view?.loadDataWithBaseURL(
                                    "https://www.youtube.com",
                                    buildYouTubeEmbedHtml(newId),
                                    "text/html",
                                    "utf-8",
                                    null
                                )
                                return true
                            }
                        }
                        return false
                    }
                }

                when {
                    youtubeId != null -> {
                        loadDataWithBaseURL(
                            "https://www.youtube.com",
                            buildYouTubeEmbedHtml(youtubeId),
                            "text/html",
                            "utf-8",
                            null
                        )
                    }
                    isDirectVideo -> {
                        loadDataWithBaseURL(
                            null,
                            buildDirectVideoHtml(mediaUrl),
                            "text/html",
                            "utf-8",
                            null
                        )
                    }
                    else -> {
                        loadUrl(mediaUrl.trim())
                    }
                }
                webViewRef = this
            }
        },
        update = { webView ->
            webViewRef = webView
        }
    )
}

private fun buildYouTubeEmbedHtml(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
          <style>
            html, body {
              margin: 0;
              padding: 0;
              width: 100%;
              height: 100%;
              background-color: #000000;
              overflow: hidden;
            }
            iframe {
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              border: 0;
            }
          </style>
        </head>
        <body>
          <iframe
            src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            allowfullscreen>
          </iframe>
        </body>
        </html>
    """.trimIndent()
}

private fun buildDirectVideoHtml(videoUrl: String): String {
    val safeUrl = videoUrl.replace("\"", "&quot;")
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <style>
            html, body { margin: 0; padding: 0; width: 100%; height: 100%; background: #000; }
            video { width: 100%; height: 100%; object-fit: contain; }
          </style>
        </head>
        <body>
          <video controls autoplay playsinline src="$safeUrl"></video>
        </body>
        </html>
    """.trimIndent()
}
