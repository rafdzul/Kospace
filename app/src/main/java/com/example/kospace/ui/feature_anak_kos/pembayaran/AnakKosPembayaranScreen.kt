package com.example.kospace.ui.feature_anak_kos.pembayaran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.ui.components.StatusBadge
import com.example.kospace.ui.components.StatusSewaPagerCard
import com.example.kospace.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnakKosPembayaranScreen(
    onNavigateToBayarTagihan: (String, Double) -> Unit,
    viewModel: PembayaranViewModel = viewModel()
) {
    val riwayatPembayaran by viewModel.riwayatPembayaran.collectAsStateWithLifecycle()

    var showProofDialog by remember { mutableStateOf<String?>(null) }
    val rupiahFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp)
    ) {
        Text(
            text = "Tagihan Kos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        // Card Grid Status Sewa — HorizontalPager per tahun
        StatusSewaPagerCard(
            titlePrefix = "Status Sewa Tahun",
            payments = riwayatPembayaran
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tombol Bayar Tagihan
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val upcomingMonths = remember { MonthPeriodHelper.generateUpcomingMonths(18) }
                val firstUnpaid = upcomingMonths.firstOrNull { opt ->
                    !viewModel.isMonthYearPaid(opt.monthNumber, opt.year)
                }

                Button(
                    onClick = {
                        val targetMonth = firstUnpaid?.label
                            ?: MonthPeriodHelper.fullMonthYear(
                                MonthPeriodHelper.currentMonthNumber(),
                                MonthPeriodHelper.currentYear()
                            )
                        onNavigateToBayarTagihan(targetMonth, 0.0)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Bayar Tagihan Kos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Riwayat Pembayaran",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // riwayatPembayaran sudah di-sort descending dari ViewModel
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(riwayatPembayaran, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.monthPeriod,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            StatusBadge(status = item.status)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = rupiahFormatter.format(item.amount),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Tanggal: ${item.paymentDate}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            OutlinedButton(
                                onClick = { showProofDialog = item.monthPeriod },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Lihat Bukti", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProofDialog != null) {
        AlertDialog(
            onDismissRequest = { showProofDialog = null },
            title = { Text("Bukti Transfer ${showProofDialog}") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(SurfaceLightBlue, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[ Simulasi Foto Bukti Transfer Resi BCA ]",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProofDialog = null }) {
                    Text("Tutup")
                }
            }
        )
    }
}
