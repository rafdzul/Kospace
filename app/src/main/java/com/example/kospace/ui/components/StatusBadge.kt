package com.example.kospace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.ui.theme.*

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "lunas", "terkirim", "selesai", "aktif" -> StatusGreenContainer to StatusGreen
        "diproses", "menunggu", "tunggak", "verifikasi" -> StatusOrangeContainer to StatusOrange
        "pending" -> StatusOrangeContainer to StatusOrange
        "tolak", "batal", "hapus", "nonaktif" -> StatusRedContainer to StatusRed
        else -> SurfaceLightBlue to TextSecondary
    }

    Text(
        text = status,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
