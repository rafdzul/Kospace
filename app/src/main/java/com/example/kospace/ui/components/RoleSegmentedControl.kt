package com.example.kospace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.ui.theme.PrimaryBlue
import com.example.kospace.ui.theme.SurfaceLightBlue
import com.example.kospace.ui.theme.TextSecondary

@Composable
fun RoleSegmentedControl(
    selectedRole: String, // "ANAK_KOS" / "IBU_KOS"
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLightBlue)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val isAnakKos = selectedRole == "ANAK_KOS"
        val isIbuKos = selectedRole == "IBU_KOS"

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isAnakKos) PrimaryBlue else Color.Transparent)
                .clickable { onRoleSelected("ANAK_KOS") }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Anak Kos",
                fontSize = 14.sp,
                fontWeight = if (isAnakKos) FontWeight.Bold else FontWeight.Medium,
                color = if (isAnakKos) Color.White else TextSecondary
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isIbuKos) PrimaryBlue else Color.Transparent)
                .clickable { onRoleSelected("IBU_KOS") }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ibu Kos",
                fontSize = 14.sp,
                fontWeight = if (isIbuKos) FontWeight.Bold else FontWeight.Medium,
                color = if (isIbuKos) Color.White else TextSecondary
            )
        }
    }
}
