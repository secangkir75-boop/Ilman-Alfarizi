package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.components.GoldBadge
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldRoyal

@Composable
fun StudentProfileScreen(
    currentUser: UserAccountEntity,
    isDesignerAccount: Boolean,
    previewAsStudent: Boolean,
    onTogglePreviewAsStudent: (Boolean) -> Unit,
    onSaveProfile: (
        fullName: String,
        schoolOrCampus: String,
        originRegion: String,
        arabicLevel: String,
        phone: String,
        learningGoal: String
    ) -> Unit,
    onOpenDesignerInfo: () -> Unit,
    onOpenDesignerDashboard: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember(currentUser.email) { mutableStateOf(currentUser.fullName) }
    var schoolOrCampus by remember(currentUser.email) { mutableStateOf(currentUser.schoolOrCampus) }
    var originRegion by remember(currentUser.email) { mutableStateOf(currentUser.originRegion) }
    var arabicLevel by remember(currentUser.email) { mutableStateOf(currentUser.arabicLevel) }
    var phone by remember(currentUser.email) { mutableStateOf(currentUser.phone) }
    var learningGoal by remember(currentUser.email) { mutableStateOf(currentUser.learningGoal) }

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
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = GoldRoyal
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser.fullName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentUser.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldRoyal
                        )
                    }
                    GoldBadge(if (isDesignerAccount) "Perancang" else "Pelajar")
                }
            }
        }

        if (isDesignerAccount) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.2.dp, GoldRoyal),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Kontrol Akses Perancang",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Pratinjau Tampilan Pelajar", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Aktifkan untuk melihat aplikasi seperti akun pelajar.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = previewAsStudent,
                                onCheckedChange = onTogglePreviewAsStudent
                            )
                        }
                        Button(
                            onClick = onOpenDesignerDashboard,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Buka Panel Kelola Perancang")
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Data Diri Pelajar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nama Lengkap") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = schoolOrCampus,
                        onValueChange = { schoolOrCampus = it },
                        label = { Text("Asal Sekolah / Kampus / Pesantren") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = originRegion,
                        onValueChange = { originRegion = it },
                        label = { Text("Asal Daerah / Kota") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Nomor HP / WhatsApp") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Tingkatan Kemampuan Bahasa Arab:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Pemula (Mubtadi')", "Menengah", "Mahir").forEach { lvl ->
                            FilterChip(
                                selected = arabicLevel == lvl,
                                onClick = { arabicLevel = lvl },
                                label = { Text(lvl, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = learningGoal,
                        onValueChange = { learningGoal = it },
                        label = { Text("Target & Catatan Belajar") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            onSaveProfile(
                                fullName,
                                schoolOrCampus,
                                originRegion,
                                arabicLevel,
                                phone,
                                learningGoal
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldVibrant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_profile_button")
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Simpan Data Diri", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = onOpenDesignerInfo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Filled.Badge, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Lihat Data Perancang Aplikasi")
            }
        }

        item {
            Button(
                onClick = onSignOut,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("sign_out_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Keluar dari Akun", fontWeight = FontWeight.Bold)
            }
        }
    }
}
