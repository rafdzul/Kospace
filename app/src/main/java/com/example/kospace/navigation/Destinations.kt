package com.example.kospace.navigation

import kotlinx.serialization.Serializable

enum class UserRole {
    ANAK_KOS, IBU_KOS
}

// Auth Routes
@Serializable
object LoginRoute

@Serializable
data class RegisterRoute(val initialRole: String = "ANAK_KOS")

// ===== NAVIGASI ANAK KOS =====
@Serializable
object AnakKosBerandaRoute

@Serializable
object AnakKosProfilRoute

@Serializable
object AnakKosKerusakanListRoute

@Serializable
object AnakKosLaporKerusakanRoute

@Serializable
data class AnakKosDetailKerusakanRoute(val reportId: String)

@Serializable
object AnakKosKulkasRoute

@Serializable
object AnakKosTambahKulkasRoute

@Serializable
object AnakKosPembayaranRoute

@Serializable
data class AnakKosBayarTagihanRoute(val bulan: String = "November 2026", val nominal: Double = 1500000.0)

// ===== NAVIGASI IBU KOS =====
@Serializable
object IbuKosBerandaRoute

@Serializable
object IbuKosProfilRoute

@Serializable
object IbuKosPenindakanListRoute

@Serializable
data class IbuKosDetailPenindakanRoute(val reportId: String)

@Serializable
object IbuKosManajemenPenghuniRoute

@Serializable
object IbuKosKonfirmasiPendaftaranRoute

@Serializable
data class IbuKosDetailPenghuniRoute(val memberId: String)
