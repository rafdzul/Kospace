package com.example.kospace.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PembayaranKos(
    val id: String,
    @SerialName("tenant_id") val tenantId: String,
    @SerialName("tenant_name") val tenantName: String,
    @SerialName("room_number") val roomNumber: String,
    @SerialName("month_period") val monthPeriod: String, // "November 2026"
    val amount: Double,
    @SerialName("payment_date") val paymentDate: String,
    val status: String, // "Lunas", "Tunggak", "Verifikasi"
    @SerialName("proof_image_url") val proofImageUrl: String? = null,
    val note: String? = null
)
