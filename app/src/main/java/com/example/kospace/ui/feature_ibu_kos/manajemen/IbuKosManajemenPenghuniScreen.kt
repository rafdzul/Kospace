package com.example.kospace.ui.feature_ibu_kos.manajemen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.ui.components.StatusBadge
import com.example.kospace.ui.theme.*

@Composable
fun IbuKosManajemenPenghuniScreen(
    onNavigateToKonfirmasi: () -> Unit,
    onNavigateToDetailPenghuni: (String) -> Unit,
    viewModel: ManajemenPenghuniViewModel = viewModel()
) {
    val penghuniAktif by viewModel.penghuniAktif.collectAsStateWithLifecycle()
    val pengajuanPending by viewModel.pengajuanPending.collectAsStateWithLifecycle()
    val riwayatPembayaran by viewModel.riwayatPembayaran.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Manajemen Anak Kos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            if (pengajuanPending.isNotEmpty()) {
                BadgedBox(
                    badge = {
                        Badge { Text("${pengajuanPending.size}") }
                    },
                    modifier = Modifier.clickable { onNavigateToKonfirmasi() }
                ) {
                    IconButton(onClick = onNavigateToKonfirmasi) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi Pengajuan",
                            tint = PrimaryBlue
                        )
                    }
                }
            }
        }

        // Summary Card
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
                        text = "Total Penghuni Aktif",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${penghuniAktif.size} Orang Terdaftar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (pengajuanPending.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onNavigateToKonfirmasi,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Konfirmasi (${pengajuanPending.size})", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Daftar Penghuni Aktif",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (penghuniAktif.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada penghuni aktif terdaftar.", color = TextSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(penghuniAktif, key = { it.id }) { user ->
                    // Hitung status sewa berdasarkan pembayaran real di tahun berjalan
                    val userPayments = remember(riwayatPembayaran) {
                        riwayatPembayaran.filter { it.tenantId == user.id }
                    }
                    val currentYear = MonthPeriodHelper.currentYear()
                    val paidCount = remember(userPayments) {
                        (1..12).count { month ->
                            MonthPeriodHelper.isMonthPaidInPayments(userPayments, month, currentYear)
                        }
                    }
                    val statusSewa = when {
                        paidCount >= 12 -> "Lunas"
                        paidCount > 0 -> "Aktif"
                        else -> "Tunggak"
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetailPenghuni(user.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceLightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${user.roomNumber ?: "Kamar -"} • ${user.phone}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            StatusBadge(status = statusSewa)

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
}
