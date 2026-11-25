package com.example.kospace.ui.feature_ibu_kos.manajemen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.components.StatusSewaPagerCard
import com.example.kospace.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun IbuKosDetailPenghuniScreen(
    memberId: String,
    onBackClick: () -> Unit,
    viewModel: ManajemenPenghuniViewModel = viewModel()
) {
    val penghuniList by viewModel.penghuniAktif.collectAsStateWithLifecycle()
    val riwayatPembayaran by viewModel.riwayatPembayaran.collectAsStateWithLifecycle()

    val tenant = penghuniList.find { it.id == memberId }
    var selectedProofPayment by remember { mutableStateOf<PembayaranKos?>(null) }
    val rupiahFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            KospaceTopBar(
                title = "Detail Anak Kos",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        if (tenant == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Penghuni tidak ditemukan", color = TextSecondary)
            }
        } else {
            val userPayments = remember(riwayatPembayaran, tenant.id) {
                riwayatPembayaran.filter { it.tenantId == tenant.id }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(BackgroundLight)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(SurfaceLightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = tenant.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Terdaftar di Kos",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Detail Contact Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Informasi Penghuni",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        InfoRow("Nomor Kamar", tenant.roomNumber ?: "-")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = CardBorderDivider)
                        InfoRow("No. WhatsApp", tenant.phone)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = CardBorderDivider)
                        InfoRow("Email", tenant.email)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid Status Pembayaran — HorizontalPager per tahun (format seragam)
                StatusSewaPagerCard(
                    titlePrefix = "Status Pembayaran Sewa",
                    payments = userPayments,
                    onPaidMonthClick = { payment -> selectedProofPayment = payment }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hubungi via WhatsApp", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.keluarkanPenghuni(tenant.id)
                        onBackClick()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluarkan Penghuni Ini")
                }
            }
        }
    }

    selectedProofPayment?.let { payment ->
        AlertDialog(
            onDismissRequest = { selectedProofPayment = null },
            title = { Text("Bukti Transfer ${payment.monthPeriod}") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Nominal: ${rupiahFormatter.format(payment.amount)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlue
                    )
                    Text(
                        text = "Tanggal Bayar: ${payment.paymentDate}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    if (!payment.note.isNullOrBlank()) {
                        Text(
                            text = "Catatan: ${payment.note}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(SurfaceLightBlue, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[ Bukti Transfer: ${payment.proofImageUrl ?: "Resi Transfer"} ]",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedProofPayment = null }) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}
