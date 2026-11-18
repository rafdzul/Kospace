package com.example.kospace.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "ANAK_KOS" / "IBU_KOS"
    val roomNumber: String? = null,
    val kostName: String = "Kost Harmoni",
    val avatarUrl: String? = null,
    val statusPenghuni: String = "Aktif" // "Aktif", "Pending", "Nonaktif"
) {
    val isIbuKos: Boolean get() = role == "IBU_KOS"
    val isAnakKos: Boolean get() = role == "ANAK_KOS"
}
