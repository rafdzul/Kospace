package com.example.kospace.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaporanKerusakan(
    val id: String,
    @SerialName("ticket_code") val ticketCode: String,
    @SerialName("tenant_id") val tenantId: String,
    @SerialName("tenant_name") val tenantName: String,
    @SerialName("room_number") val roomNumber: String,
    val title: String,
    @SerialName("category_location") val categoryLocation: String, // Misal: "Kamar Mandi Lt. 2", "Kamar 04 - AC"
    val description: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val status: String, // "Pending", "Diproses", "Selesai"
    @SerialName("report_date") val reportDate: String, // "Hari ini, 09:15"
    @SerialName("action_note") val actionNote: String? = null,
    @SerialName("technician_info") val technicianInfo: String? = null
) {
    val isSelesai: Boolean get() = status == "Selesai"
    val isPending: Boolean get() = status == "Pending"
    val isDiproses: Boolean get() = status == "Diproses"
}
