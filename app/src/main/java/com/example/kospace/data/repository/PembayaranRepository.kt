package com.example.kospace.data.repository

import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.PembayaranKos
import kotlinx.coroutines.flow.StateFlow

interface PembayaranRepository {
    val monthStatuses: StateFlow<List<MonthStatus>>
    val riwayatPembayaran: StateFlow<List<PembayaranKos>>
    fun kirimBuktiBayar(pembayaran: PembayaranKos)
}
