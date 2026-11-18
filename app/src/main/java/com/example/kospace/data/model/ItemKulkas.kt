package com.example.kospace.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemKulkas(
    val id: String,
    val ownerId: String,
    val ownerName: String,
    val roomNumber: String,
    val itemName: String,
    val quantity: Int,
    val unit: String, // "Botol", "Porsi", "Lembar", "Slice", "Pcs"
    val expirationDate: String,
    val note: String? = null,
    val imageUrl: String? = null,
    val addedDate: String
)
