package com.example.kospace.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PembayaranKos(
    val id: String,
    val tenantId: String,
    val tenantName: String,
    val roomNumber: String,
    val monthPeriod: String, // "November 2026"
    val amount: Double,
    val paymentDate: String,
    val status: String, // "Lunas", "Tunggak", "Verifikasi"
    val proofImageUrl: String? = null,
    val note: String? = null
)
