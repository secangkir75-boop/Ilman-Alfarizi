package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.CertificateEntity
import com.example.ui.components.DigitalCertificateDialog
import com.example.ui.components.EmeraldBadge
import com.example.ui.components.GoldBadge
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GoldRoyal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CertificatesScreen(
    certificates: List<CertificateEntity>,
    onPromptAcceptCertificate: (CertificateEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var viewingCertificate by remember { mutableStateOf<CertificateEntity?>(null) }
    val pending = remember(certificates) { certificates.filter { !it.isAcceptedByStudent } }
    val accepted = remember(certificates) { certificates.filter { it.isAcceptedByStudent } }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldNight),
                border = BorderStroke(1.5.dp, GoldRoyal)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_certificate_seal),
                        contentDescription = "Stempel Sertifikat",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            text = "Sertifikat Iḥyā’ul ‘Arabiyyah",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Diterbitkan oleh Perancang (Maqbūl – Mumtāz)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldRoyal
                        )
                    }
                }
            }
        }

        if (pending.isNotEmpty()) {
            item {
                Text(
                    text = "Prompt Sertifikat Menunggu Diterima",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GoldDeep
                )
            }
            items(pending, key = { it.id }) { cert ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF6D6)),
                    border = BorderStroke(1.5.dp, GoldRoyal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GoldBadge("Baru Dikirim Perancang")
                            Text(
                                text = cert.certificateCode,
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldDeep
                            )
                        }
                        Text(
                            text = "Sertifikat Predikat ${cert.predicate}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldNight
                        )
                        Text(
                            text = "Nilai Evaluasi: ${cert.score} • Pengirim: ${cert.issuedBy}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EmeraldDeep
                        )
                        Button(
                            onClick = { onPromptAcceptCertificate(cert) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_accept_prompt_${cert.id}")
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Buka Prompt & Terima Sertifikat", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Koleksi Sertifikat Diterima (${accepted.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (accepted.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldRoyal,
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            text = "Belum ada sertifikat yang diterima",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Selesaikan ujian evaluasi Bahasa Arab dan tunggu kiriman sertifikat dari Perancang (${com.example.data.model.DesignerProfileData.NAME}).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(accepted, key = { it.id }) { cert ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, GoldRoyal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewingCertificate = cert }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldRoyal,
                            modifier = Modifier.size(38.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Predikat ${cert.predicate}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${cert.studentName} • Nilai ${cert.score}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Diterima ${dateFormat.format(Date(cert.acceptedAt ?: cert.issuedAt))}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        EmeraldBadge("Lihat")
                    }
                }
            }
        }
    }

    if (viewingCertificate != null) {
        DigitalCertificateDialog(
            certificate = viewingCertificate!!,
            onDismiss = { viewingCertificate = null }
        )
    }
}
