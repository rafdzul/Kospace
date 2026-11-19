package com.example.kospace.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MonthStatus(
    val monthName: String,
    val monthNumber: Int,
    val isPaid: Boolean
)
