package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DesignerProfileData
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.GoldRoyal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignerInfoScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Data Perancang Aplikasi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldNight,
                    titleContentColor = Color.White,
                    navigationIconContentColor = GoldRadiant
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldNight),
                    border = BorderStroke(2.dp, GoldRoyal)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_designer_avatar),
                            contentDescription = "Foto Ilustrasi Perancang Ilman Alfarizi",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(104.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, GoldRadiant, CircleShape)
                        )
                        com.example.ui.components.GoldCalligraphyText(
                            text = "مُصَمِّمُ التَّطْبِيقِ",
                            fontSize = 22.sp
                        )
                        Text(
                            text = DesignerProfileData.NAME,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Mahasiswa ${DesignerProfileData.UNIVERSITY} • NIM ${DesignerProfileData.NIM}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldRadiant
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, GoldRoyal.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Biodata Lengkap Perancang",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider()
                        DesignerDetailRow(Icons.Filled.Person, "Nama Lengkap", DesignerProfileData.NAME)
                        DesignerDetailRow(Icons.Filled.Cake, "Tempat, Tanggal Lahir", DesignerProfileData.BIRTH_PLACE_DATE)
                        DesignerDetailRow(Icons.Filled.LocationOn, "Asal Daerah", DesignerProfileData.ORIGIN_REGION)
                        DesignerDetailRow(Icons.Filled.School, "Mahasiswa", DesignerProfileData.UNIVERSITY)
                        DesignerDetailRow(Icons.Filled.Numbers, "NIM", DesignerProfileData.NIM)
                        DesignerDetailRow(Icons.Filled.Call, "No. HP", DesignerProfileData.PHONE)
                        DesignerDetailRow(Icons.Filled.Email, "Email Perancang", DesignerProfileData.DESIGNER_EMAIL)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            runCatching {
                                context.startActivity(
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:${DesignerProfileData.PHONE}"))
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Hubungi HP", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            runCatching {
                                context.startActivity(
                                    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${DesignerProfileData.DESIGNER_EMAIL}"))
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Kirim Email", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DesignerDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldVibrant,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
