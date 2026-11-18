package com.example.kospace.ui.feature_anak_kos.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.kospace.data.repository.DummyAuthRepository
import com.example.kospace.data.repository.DummyKerusakanRepository
import com.example.kospace.data.repository.DummyKulkasRepository
import com.example.kospace.data.repository.DummyPembayaranRepository
import com.example.kospace.ui.components.StatusBadge
import com.example.kospace.ui.theme.*

@Composable
fun AnakKosBerandaScreen(
    onNavigateToPembayaran: () -> Unit,
    onNavigateToKulkas: () -> Unit,
    onNavigateToKerusakan: () -> Unit
) {
    val user by DummyAuthRepository.currentUser.collectAsStateWithLifecycle()
    val kerusakanList by DummyKerusakanRepository.laporanList.collectAsStateWithLifecycle()
    val kulkasItems by DummyKulkasRepository.items.collectAsStateWithLifecycle()
    val monthStatuses by DummyPembayaranRepository.monthStatuses.collectAsStateWithLifecycle()

    val currentMonthPaid = monthStatuses.find { it.monthNumber == 11 }?.isPaid == true
    val myActiveReport = kerusakanList.firstOrNull { it.status != "Selesai" }
    val myFridgeItems = kulkasItems.filter { it.ownerId == user?.id }

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
                    text = "Halo, ${user?.name ?: "Anak Kos"} 👋",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${user?.roomNumber ?: "Kamar 04"} • ${user?.kostName ?: "Kost Harmoni"}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card Status Sewa Bulan Ini
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Status Sewa Bulan Ini",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "November 2026",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        StatusBadge(status = if (currentMonthPaid) "Lunas" else "Tunggak")
                    }
                }

                Button(
                    onClick = onNavigateToPembayaran,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Lihat Detail",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Access / Active Feature Overview
        Text(
            text = "Laporan Kerusakan Terkini",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToKerusakan() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceLightBlue, CircleShape)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (myActiveReport != null) {
                        Text(
                            text = myActiveReport.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = myActiveReport.categoryLocation,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    } else {
                        Text(
                            text = "Tidak Ada Laporan Aktif",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Fasilitas kamar dalam kondisi baik",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                if (myActiveReport != null) {
                    StatusBadge(status = myActiveReport.status)
                } else {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Widget Barang Saya di Kulkas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Barang Saya di Kulkas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Lihat Semua >",
                fontSize = 12.sp,
                color = PrimaryBlue,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavigateToKulkas() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (myFridgeItems.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Belum ada makanan/minuman yang Anda simpan di kulkas bersama.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                myFridgeItems.take(2).forEach { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Kitchen,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(SurfaceLightBlue, RoundedCornerShape(8.dp))
                                    .padding(6.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.itemName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Kedaluwarsa: ${item.expirationDate}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = "${item.quantity} ${item.unit}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
