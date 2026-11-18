package com.example.kospace.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.kospace.navigation.*
import com.example.kospace.ui.theme.PrimaryBlue
import com.example.kospace.ui.theme.TextSecondary

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

val anakKosNavItems = listOf(
    BottomNavItem("Beranda", Icons.Default.Home, AnakKosBerandaRoute),
    BottomNavItem("Tagihan", Icons.Default.Receipt, AnakKosPembayaranRoute),
    BottomNavItem("Kulkas", Icons.Default.Kitchen, AnakKosKulkasRoute),
    BottomNavItem("Kerusakan", Icons.Default.Build, AnakKosKerusakanListRoute),
    BottomNavItem("Profil", Icons.Default.Person, AnakKosProfilRoute)
)

val ibuKosNavItems = listOf(
    BottomNavItem("Beranda", Icons.Default.Home, IbuKosBerandaRoute),
    BottomNavItem("Kerusakan", Icons.Default.Build, IbuKosPenindakanListRoute),
    BottomNavItem("Manajemen", Icons.Default.People, IbuKosManajemenPenghuniRoute),
    BottomNavItem("Profil", Icons.Default.Person, IbuKosProfilRoute)
)

@Composable
fun KospaceBottomBar(
    role: String, // "ANAK_KOS" / "IBU_KOS"
    currentRoute: String?,
    onNavigate: (Any) -> Unit
) {
    val items = if (role == "IBU_KOS") ibuKosNavItems else anakKosNavItems

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val targetRouteName = item.route::class.qualifiedName
            val isSelected = currentRoute == targetRouteName

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    indicatorColor = Color(0xFFF0F4F9),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
