package com.example.kospace.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LaporanKerusakan(
    val id: String,
    val ticketCode: String,
    val tenantId: String,
    val tenantName: String,
    val roomNumber: String,
    val title: String,
    val categoryLocation: String, // Misal: "Kamar Mandi Lt. 2", "Kamar 04 - AC"
    val description: String,
    val imageUrl: String? = null,
    val status: String, // "Pending", "Diproses", "Selesai"
    val reportDate: String, // "Hari ini, 09:15"
    val actionNote: String? = null,
    val technicianInfo: String? = null
) {
    val isSelesai: Boolean get() = status == "Selesai"
    val isPending: Boolean get() = status == "Pending"
    val isDiproses: Boolean get() = status == "Diproses"
}
