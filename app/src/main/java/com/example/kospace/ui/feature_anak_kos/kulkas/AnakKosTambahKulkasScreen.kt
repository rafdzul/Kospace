package com.example.kospace.ui.feature_anak_kos.kulkas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnakKosTambahKulkasScreen(
    onBackClick: () -> Unit,
    onItemAdded: () -> Unit,
    viewModel: KulkasViewModel = viewModel()
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var unit by remember { mutableStateOf("Botol") }
    var expirationDate by remember { mutableStateOf("15 Nov 2026") }
    var note by remember { mutableStateOf("") }
    var isPhotoUploaded by remember { mutableStateOf(false) }

    val unitOptions = listOf("Botol", "Porsi", "Lembar", "Slice", "Pcs", "Pack")
    var unitDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KospaceTopBar(
                title = "Tambah Barang ke Kulkas",
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
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Nama Makanan / Minuman") },
                        placeholder = { Text("misal: Susu UHT, Buah Apel") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Jumlah & Satuan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Stepper
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(SurfaceLightBlue, RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Kurang")
                            }
                            Text(
                                text = "$quantity",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { quantity++ }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah")
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Satuan Dropdown
                        ExposedDropdownMenuBox(
                            expanded = unitDropdownExpanded,
                            onExpandedChange = { unitDropdownExpanded = !unitDropdownExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = unit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Satuan") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitDropdownExpanded) },
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = unitDropdownExpanded,
                                onDismissRequest = { unitDropdownExpanded = false }
                            ) {
                                unitOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            unit = option
                                            unitDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = expirationDate,
                        onValueChange = { expirationDate = it },
                        label = { Text("Tanggal Kedaluwarsa") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Unggah Foto Wadah / Label",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                if (isPhotoUploaded) StatusGreenContainer else SurfaceLightBlue,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isPhotoUploaded) StatusGreen else CardBorderDivider,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { isPhotoUploaded = !isPhotoUploaded },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = if (isPhotoUploaded) StatusGreen else PrimaryBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isPhotoUploaded) "✓ Foto Terunggah" else "Klik untuk upload foto wadah",
                                fontSize = 12.sp,
                                color = if (isPhotoUploaded) StatusGreen else TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Catatan / Label Wadah (Opsional)") },
                        placeholder = { Text("misal: Label: Rian 04") },
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
                                if (itemName.isNotBlank()) {
                                    viewModel.tambahItem(
                                        itemName = itemName,
                                        quantity = quantity,
                                        unit = unit,
                                        expirationDate = expirationDate,
                                        note = note
                                    )
                                    onItemAdded()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Simpan ke Kulkas", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
