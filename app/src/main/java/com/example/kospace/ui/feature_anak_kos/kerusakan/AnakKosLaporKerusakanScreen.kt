package com.example.kospace.ui.feature_anak_kos.kerusakan

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.theme.*

@Composable
fun AnakKosLaporKerusakanScreen(
    onBackClick: () -> Unit,
    onReportSubmitted: () -> Unit,
    viewModel: KerusakanViewModel = viewModel()
) {
    var locationCategory by remember { mutableStateOf("Kamar Mandi Dalam") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isImageUploaded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KospaceTopBar(
                title = "Buat Laporan Kerusakan",
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
                    OutlinedTextField(
                        value = locationCategory,
                        onValueChange = { locationCategory = it },
                        label = { Text("Lokasi Fasilitas") },
                        placeholder = { Text("misal: Kamar Mandi Dalam, AC Kamar 04") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Judul Keluhan / Kerusakan") },
                        placeholder = { Text("misal: Kran air patah, AC tidak dingin") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Deskripsi Rinci / Catatan") },
                        placeholder = { Text("Jelaskan kondisi detail kerusakan yang dialami...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Unggah Foto Kerusakan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
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
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isImageUploaded) "✓ Foto Kerusakan Terlampir" else "Klik untuk upload foto fasilitas rusak",
                                fontSize = 12.sp,
                                color = if (isImageUploaded) StatusGreen else TextSecondary
                            )
                        }
                    }

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
                                if (title.isNotBlank()) {
                                    viewModel.tambahLaporan(
                                        title = title,
                                        locationCategory = locationCategory,
                                        description = description,
                                        imageUrl = "damage_sample.jpg"
                                    )
                                    onReportSubmitted()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Kirim Laporan", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
