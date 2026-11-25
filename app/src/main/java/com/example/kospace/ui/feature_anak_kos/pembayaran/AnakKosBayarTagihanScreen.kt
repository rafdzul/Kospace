package com.example.kospace.ui.feature_anak_kos.pembayaran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.ui.components.KospaceTopBar
import com.example.kospace.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnakKosBayarTagihanScreen(
    bulan: String,
    nominal: Double,
    onBackClick: () -> Unit,
    onPaymentSubmitted: () -> Unit,
    viewModel: PembayaranViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val riwayatPembayaran by viewModel.riwayatPembayaran.collectAsStateWithLifecycle()

    val upcomingMonths = remember { MonthPeriodHelper.generateUpcomingMonths(18) }

    val paidLabels = remember(riwayatPembayaran, upcomingMonths) {
        upcomingMonths
            .filter { opt ->
                MonthPeriodHelper.isMonthPaidInPayments(riwayatPembayaran, opt.monthNumber, opt.year)
            }
            .map { it.label }
            .toSet()
    }

    var selectedMonths by remember(bulan, paidLabels) {
        val defaultSelection = upcomingMonths
            .firstOrNull { it.label.equals(bulan, ignoreCase = true) && it.label !in paidLabels }
            ?.label
        mutableStateOf(if (defaultSelection != null) listOf(defaultSelection) else emptyList())
    }
    var nominalInput by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isImageUploaded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

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
                        value = currentUser?.name ?: "-",
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Pembayaran Atas Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pembayaran Untuk Bulan (Bisa pilih lebih dari satu & lintas tahun)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (selectedMonths.isEmpty()) {
                            "Pilih minimal 1 bulan yang belum lunas"
                        } else {
                            "Dipilih (${selectedMonths.size} bulan): ${selectedMonths.joinToString(", ")}"
                        },
                        fontSize = 12.sp,
                        color = if (selectedMonths.isEmpty()) TextSecondary else PrimaryBlue,
                        fontWeight = if (selectedMonths.isEmpty()) FontWeight.Normal else FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        upcomingMonths.forEach { option ->
                            val isPaid = option.label in paidLabels
                            val isSelected = option.label in selectedMonths
                            val shortLabel = "${MonthPeriodHelper.shortMonthNames[option.monthNumber - 1]} ${option.year}"

                            FilterChip(
                                selected = isSelected,
                                enabled = !isPaid,
                                onClick = {
                                    selectedMonths = if (isSelected) {
                                        selectedMonths - option.label
                                    } else {
                                        (selectedMonths + option.label).sortedBy { label ->
                                            upcomingMonths.indexOfFirst { it.label == label }
                                        }
                                    }
                                    errorMessage = null
                                },
                                label = {
                                    Text(
                                        text = if (isPaid) "$shortLabel (Lunas)" else shortLabel,
                                        fontSize = 12.sp
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SurfaceLightBlue,
                                    selectedLabelColor = PrimaryBlue,
                                    selectedLeadingIconColor = PrimaryBlue,
                                    disabledContainerColor = StatusGreenContainer,
                                    disabledLabelColor = StatusGreen
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = nominalInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                nominalInput = input
                                errorMessage = null
                            }
                        },
                        label = { Text("Nominal yang Ditransfer (Rp)") },
                        placeholder = { Text("Masukkan nominal transfer, misal: 1500000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
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
                            .clickable {
                                isImageUploaded = !isImageUploaded
                                errorMessage = null
                            },
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

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
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
                                val amountVal = nominalInput.toDoubleOrNull()
                                when {
                                    selectedMonths.isEmpty() -> {
                                        errorMessage = "Silakan pilih minimal 1 bulan pembayaran"
                                    }
                                    amountVal == null || amountVal <= 0 -> {
                                        errorMessage = "Masukkan nominal transfer yang valid (lebih dari 0)"
                                    }
                                    !isImageUploaded -> {
                                        errorMessage = "Silakan unggah bukti transfer terlebih dahulu"
                                    }
                                    else -> {
                                        viewModel.kirimBuktiBayar(
                                            monthPeriods = selectedMonths,
                                            totalAmount = amountVal,
                                            note = note
                                        )
                                        onPaymentSubmitted()
                                    }
                                }
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
