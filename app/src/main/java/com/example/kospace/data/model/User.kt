package com.example.kospace.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "ANAK_KOS" / "IBU_KOS"
    @SerialName("room_number") val roomNumber: String? = null,
    @SerialName("kost_name") val kostName: String = "Kost Harmoni",
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("status_penghuni") val statusPenghuni: String = "Aktif" // "Aktif", "Pending", "Nonaktif"
) {
    val isIbuKos: Boolean get() = role == "IBU_KOS"
    val isAnakKos: Boolean get() = role == "ANAK_KOS"
}
