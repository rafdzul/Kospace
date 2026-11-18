package com.example.kospace.ui.feature_ibu_kos.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.data.repository.DummyAnakKosRepository
import com.example.kospace.data.repository.DummyAuthRepository
import com.example.kospace.data.repository.DummyKerusakanRepository
import com.example.kospace.ui.components.StatusBadge
import com.example.kospace.ui.theme.*

@Composable
fun IbuKosBerandaScreen(
    onNavigateToPenindakan: () -> Unit,
    onNavigateToManajemen: () -> Unit,
    onNavigateToKonfirmasi: () -> Unit
) {
    val user by DummyAuthRepository.currentUser.collectAsStateWithLifecycle()
    val kerusakanList by DummyKerusakanRepository.laporanList.collectAsStateWithLifecycle()
    val penghuniList by DummyAnakKosRepository.penghuniAktif.collectAsStateWithLifecycle()
    val pendingList by DummyAnakKosRepository.pengajuanPending.collectAsStateWithLifecycle()

    val pendingReportCount = kerusakanList.count { it.status == "Pending" || it.status == "Diproses" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Halo, ${user?.name ?: "Ibu Hj. Maryam"} 👋",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Pemilik ${user?.kostName ?: "Kost Harmoni"}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stat Card Overview
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Ringkasan Operasional Kos",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${penghuniList.size} Penghuni",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "3 Lunas • 1 Tunggak",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    if (pendingList.isNotEmpty()) {
                        Button(
                            onClick = onNavigateToKonfirmasi,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusOrange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${pendingList.size} Pengajuan Baru",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Access Cards
        Text(
            text = "Akses Cepat Pengelolaan",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToPenindakan() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier
                            .size(36.dp)
                            .background(SurfaceLightBlue, CircleShape)
                            .padding(6.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Penindakan Kerusakan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "$pendingReportCount Laporan Perlu Ditindak",
                        fontSize = 11.sp,
                        color = if (pendingReportCount > 0) StatusOrange else TextSecondary,
                        fontWeight = if (pendingReportCount > 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToManajemen() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier
                            .size(36.dp)
                            .background(SurfaceLightBlue, CircleShape)
                            .padding(6.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Manajemen Anak Kos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${penghuniList.size} Penghuni Aktif Terdaftar",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Laporan Kerusakan Terbaru
        Text(
            text = "Laporan Kerusakan Terbaru",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            kerusakanList.take(3).forEach { report ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPenindakan() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = report.tenantName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = " (${report.roomNumber})",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = report.title,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = "Dilaporkan: ${report.reportDate}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        StatusBadge(status = report.status)

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
