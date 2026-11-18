package com.example.kospace.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kospace.navigation.*
import com.example.kospace.ui.components.KospaceBottomBar
import com.example.kospace.ui.feature_anak_kos.beranda.AnakKosBerandaScreen
import com.example.kospace.ui.feature_anak_kos.kerusakan.AnakKosDetailKerusakanScreen
import com.example.kospace.ui.feature_anak_kos.kerusakan.AnakKosKerusakanListScreen
import com.example.kospace.ui.feature_anak_kos.kerusakan.AnakKosLaporKerusakanScreen
import com.example.kospace.ui.feature_anak_kos.kulkas.AnakKosKulkasScreen
import com.example.kospace.ui.feature_anak_kos.kulkas.AnakKosTambahKulkasScreen
import com.example.kospace.ui.feature_anak_kos.pembayaran.AnakKosBayarTagihanScreen
import com.example.kospace.ui.feature_anak_kos.pembayaran.AnakKosPembayaranScreen
import com.example.kospace.ui.feature_anak_kos.profil.AnakKosProfilScreen
import com.example.kospace.ui.feature_auth.AuthViewModel
import com.example.kospace.ui.feature_auth.LoginScreen
import com.example.kospace.ui.feature_auth.RegisterScreen
import com.example.kospace.ui.feature_ibu_kos.beranda.IbuKosBerandaScreen
import com.example.kospace.ui.feature_ibu_kos.manajemen.IbuKosDetailPenghuniScreen
import com.example.kospace.ui.feature_ibu_kos.manajemen.IbuKosKonfirmasiPendaftaranScreen
import com.example.kospace.ui.feature_ibu_kos.manajemen.IbuKosManajemenPenghuniScreen
import com.example.kospace.ui.feature_ibu_kos.penindakan.IbuKosDetailPenindakanScreen
import com.example.kospace.ui.feature_ibu_kos.penindakan.IbuKosPenindakanListScreen
import com.example.kospace.ui.feature_ibu_kos.profil.IbuKosProfilScreen

@Composable
fun KospaceNavHost(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val role = currentUser?.role ?: UserRole.ANAK_KOS.name
    val isLoggedIn = currentUser != null

    val isMainTabScreen = currentDestination != null && (
            currentDestination.endsWith("AnakKosBerandaRoute") ||
                    currentDestination.endsWith("AnakKosPembayaranRoute") ||
                    currentDestination.endsWith("AnakKosKulkasRoute") ||
                    currentDestination.endsWith("AnakKosKerusakanListRoute") ||
                    currentDestination.endsWith("AnakKosProfilRoute") ||
                    currentDestination.endsWith("IbuKosBerandaRoute") ||
                    currentDestination.endsWith("IbuKosPenindakanListRoute") ||
                    currentDestination.endsWith("IbuKosManajemenPenghuniRoute") ||
                    currentDestination.endsWith("IbuKosProfilRoute")
            )

    // Helper navigasi untuk antar-tab utama
    fun navigateTab(targetRoute: Any) {
        navController.navigate(targetRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (isLoggedIn && isMainTabScreen) {
                KospaceBottomBar(
                    role = role,
                    currentRoute = currentDestination,
                    onNavigate = { route ->
                        navigateTab(route)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (!isLoggedIn) LoginRoute else if (role == UserRole.IBU_KOS.name) IbuKosBerandaRoute else AnakKosBerandaRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            // AUTH
            composable<LoginRoute> {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { userRole ->
                        val startRoute = if (userRole == UserRole.IBU_KOS.name) IbuKosBerandaRoute else AnakKosBerandaRoute
                        navController.navigate(startRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { initRole ->
                        navController.navigate(RegisterRoute(initRole))
                    }
                )
            }

            composable<RegisterRoute> { entry ->
                val route = entry.toRoute<RegisterRoute>()
                RegisterScreen(
                    viewModel = authViewModel,
                    initialRole = route.initialRole,
                    onRegisterSuccess = { userRole ->
                        val startRoute = if (userRole == UserRole.IBU_KOS.name) IbuKosBerandaRoute else AnakKosBerandaRoute
                        navController.navigate(startRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigateUp()
                    }
                )
            }

            // ===== ANAK KOS =====
            composable<AnakKosBerandaRoute> {
                AnakKosBerandaScreen(
                    onNavigateToPembayaran = { navigateTab(AnakKosPembayaranRoute) },
                    onNavigateToKulkas = { navigateTab(AnakKosKulkasRoute) },
                    onNavigateToKerusakan = { navigateTab(AnakKosKerusakanListRoute) }
                )
            }

            composable<AnakKosPembayaranRoute> {
                AnakKosPembayaranScreen(
                    onNavigateToBayarTagihan = { bulan, nominal ->
                        navController.navigate(AnakKosBayarTagihanRoute(bulan, nominal))
                    }
                )
            }

            composable<AnakKosBayarTagihanRoute> { entry ->
                val args = entry.toRoute<AnakKosBayarTagihanRoute>()
                AnakKosBayarTagihanScreen(
                    bulan = args.bulan,
                    nominal = args.nominal,
                    onBackClick = { navController.navigateUp() },
                    onPaymentSubmitted = { navController.navigateUp() }
                )
            }

            composable<AnakKosKulkasRoute> {
                AnakKosKulkasScreen(
                    onNavigateToTambahKulkas = { navController.navigate(AnakKosTambahKulkasRoute) }
                )
            }

            composable<AnakKosTambahKulkasRoute> {
                AnakKosTambahKulkasScreen(
                    onBackClick = { navController.navigateUp() },
                    onItemAdded = { navController.navigateUp() }
                )
            }

            composable<AnakKosKerusakanListRoute> {
                AnakKosKerusakanListScreen(
                    onNavigateToLapor = { navController.navigate(AnakKosLaporKerusakanRoute) },
                    onNavigateToDetail = { reportId ->
                        navController.navigate(AnakKosDetailKerusakanRoute(reportId))
                    }
                )
            }

            composable<AnakKosLaporKerusakanRoute> {
                AnakKosLaporKerusakanScreen(
                    onBackClick = { navController.navigateUp() },
                    onReportSubmitted = { navController.navigateUp() }
                )
            }

            composable<AnakKosDetailKerusakanRoute> { entry ->
                val args = entry.toRoute<AnakKosDetailKerusakanRoute>()
                AnakKosDetailKerusakanScreen(
                    reportId = args.reportId,
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<AnakKosProfilRoute> {
                AnakKosProfilScreen(
                    onLogout = {
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // ===== IBU KOS =====
            composable<IbuKosBerandaRoute> {
                IbuKosBerandaScreen(
                    onNavigateToPenindakan = { navigateTab(IbuKosPenindakanListRoute) },
                    onNavigateToManajemen = { navigateTab(IbuKosManajemenPenghuniRoute) },
                    onNavigateToKonfirmasi = { navController.navigate(IbuKosKonfirmasiPendaftaranRoute) }
                )
            }

            composable<IbuKosPenindakanListRoute> {



                IbuKosPenindakanListScreen(
                    onNavigateToDetailPenindakan = { reportId ->
                        navController.navigate(IbuKosDetailPenindakanRoute(reportId))
                    }
                )
            }

            composable<IbuKosDetailPenindakanRoute> { entry ->
                val args = entry.toRoute<IbuKosDetailPenindakanRoute>()
                IbuKosDetailPenindakanScreen(
                    reportId = args.reportId,
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<IbuKosManajemenPenghuniRoute> {
                IbuKosManajemenPenghuniScreen(
                    onNavigateToKonfirmasi = { navController.navigate(IbuKosKonfirmasiPendaftaranRoute) },
                    onNavigateToDetailPenghuni = { memberId ->
                        navController.navigate(IbuKosDetailPenghuniRoute(memberId))
                    }
                )
            }

            composable<IbuKosKonfirmasiPendaftaranRoute> {
                IbuKosKonfirmasiPendaftaranScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<IbuKosDetailPenghuniRoute> { entry ->
                val args = entry.toRoute<IbuKosDetailPenghuniRoute>()
                IbuKosDetailPenghuniScreen(
                    memberId = args.memberId,
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<IbuKosProfilRoute> {
                IbuKosProfilScreen(
                    onLogout = {
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}