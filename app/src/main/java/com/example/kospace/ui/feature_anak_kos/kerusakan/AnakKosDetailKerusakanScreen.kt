package com.example.kospace.ui.feature_anak_kos.kerusakan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.components.StatusBadge
import com.example.kospace.ui.theme.*

@Composable
fun AnakKosDetailKerusakanScreen(
    reportId: String,
    onBackClick: () -> Unit,
    viewModel: KerusakanViewModel = viewModel()
) {
    val laporanList by viewModel.laporanList.collectAsStateWithLifecycle()
    val report = laporanList.find { it.id == reportId }

    Scaffold(
        topBar = {
            KospaceTopBar(
                title = "Detail Laporan Kerusakan",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        if (report == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Laporan tidak ditemukan", color = TextSecondary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(BackgroundLight)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = report.ticketCode,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                            StatusBadge(status = report.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = report.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "Lokasi: ${report.categoryLocation}",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(SurfaceLightBlue, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "[ Foto Lampiran Kerusakan Fasilitas ]",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Deskripsi Keluhan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = report.description,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        if (!report.actionNote.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = StatusGreenContainer),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Catatan Ibu Kos / Teknisi:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusGreen
                                    )
                                    Text(
                                        text = report.actionNote!!,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Progress Indicator
                        Text(
                            text = "Status Penanganan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val isPending = report.status == "Pending"
                            val isDiproses = report.status == "Diproses"
                            val isSelesai = report.status == "Selesai"

                            StepItem("Terkirim", isDone = true)
                            StepItem("Diproses", isDone = isDiproses || isSelesai)
                            StepItem("Selesai", isDone = isSelesai)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (report.status != "Selesai") {
                            OutlinedButton(
                                onClick = {
                                    viewModel.batalkanLaporan(report.id)
                                    onBackClick()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Batalkan Laporan Ini")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(label: String, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    if (isDone) StatusGreen else CardBorderDivider,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isDone) TextPrimary else TextSecondary,
            fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
        )
    }
}
