package com.example.kospace.ui.feature_anak_kos.pembayaran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.repository.DummyAuthRepository
import com.example.kospace.data.repository.DummyPembayaranRepository
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.theme.*

@Composable
fun AnakKosBayarTagihanScreen(
    bulan: String,
    nominal: Double,
    onBackClick: () -> Unit,
    onPaymentSubmitted: () -> Unit
) {
    val currentUser by DummyAuthRepository.currentUser.collectAsStateWithLifecycle()

    var selectedBulan by remember { mutableStateOf(bulan) }
    var nominalInput by remember { mutableStateOf(nominal.toLong().toString()) }
    var note by remember { mutableStateOf("") }
    var isImageUploaded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KospaceTopBar(
                title = "Bayar Tagihan Kos",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
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
                    Text(
                        text = "Informasi Pembayaran",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = currentUser?.name ?: "Dimas Saputra",
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Pembayaran Atas Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = selectedBulan,
                        onValueChange = { selectedBulan = it },
                        label = { Text("Pembayaran Untuk Bulan") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nominalInput,
                        onValueChange = { nominalInput = it },
                        label = { Text("Nominal yang Ditransfer (Rp)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Unggah Bukti Transfer / Resi",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(
                                if (isImageUploaded) StatusGreenContainer else SurfaceLightBlue,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isImageUploaded) StatusGreen else CardBorderDivider,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { isImageUploaded = !isImageUploaded },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = if (isImageUploaded) StatusGreen else PrimaryBlue,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isImageUploaded) "✓ Resi_Transfer_BCA.jpg (Siap)" else "Klik untuk simulasi unggah foto bukti transfer",
                                fontSize = 12.sp,
                                color = if (isImageUploaded) StatusGreen else TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Catatan Tambahan (Opsional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Batal")
                        }

                        Button(
                            onClick = {
                                val amountVal = nominalInput.toDoubleOrNull() ?: 1500000.0
                                val payment = PembayaranKos(
                                    id = "p_${System.currentTimeMillis()}",
                                    tenantId = currentUser?.id ?: "u2",
                                    tenantName = currentUser?.name ?: "Dimas Saputra",
                                    roomNumber = currentUser?.roomNumber ?: "Kamar 04",
                                    monthPeriod = selectedBulan,
                                    amount = amountVal,
                                    paymentDate = "Hari ini",
                                    status = "Lunas",
                                    proofImageUrl = "proof_sample.jpg",
                                    note = note.ifBlank { null }
                                )
                                DummyPembayaranRepository.kirimBuktiBayar(payment)
                                onPaymentSubmitted()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Kirim Bukti", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
