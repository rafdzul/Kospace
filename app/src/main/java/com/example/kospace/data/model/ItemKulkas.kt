package com.example.kospace.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemKulkas(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    @SerialName("owner_name") val ownerName: String,
    @SerialName("room_number") val roomNumber: String,
    @SerialName("item_name") val itemName: String,
    val quantity: Int,
    val unit: String, // "Botol", "Porsi", "Lembar", "Slice", "Pcs"
    @SerialName("expiration_date") val expirationDate: String,
    val note: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("added_date") val addedDate: String
)
