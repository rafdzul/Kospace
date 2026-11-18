package com.example.kospace.data.repository

import com.example.kospace.data.model.PembayaranKos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MonthStatus(
    val monthName: String,
    val monthNumber: Int,
    val isPaid: Boolean
)

object DummyPembayaranRepository {
    private val _monthStatuses = MutableStateFlow(
        listOf(
            MonthStatus("Jan", 1, true),
            MonthStatus("Feb", 2, true),
            MonthStatus("Mar", 3, true),
            MonthStatus("Apr", 4, true),
            MonthStatus("Mei", 5, true),
            MonthStatus("Jun", 6, true),
            MonthStatus("Jul", 7, true),
            MonthStatus("Agu", 8, true),
            MonthStatus("Sep", 9, true),
            MonthStatus("Okt", 10, true),
            MonthStatus("Nov", 11, false),
            MonthStatus("Des", 12, false)
        )
    )
    val monthStatuses: StateFlow<List<MonthStatus>> = _monthStatuses.asStateFlow()

    private val _riwayatPembayaran = MutableStateFlow(
        listOf(
            PembayaranKos(
                id = "p1",
                tenantId = "u2",
                tenantName = "Dimas Saputra",
                roomNumber = "Kamar 04",
                monthPeriod = "November 2026",
                amount = 1500000.0,
                paymentDate = "01 Nov 2026",
                status = "Lunas",
                proofImageUrl = "proof_nov.jpg",
                note = "Transfer via BCA"
            ),
            PembayaranKos(
                id = "p2",
                tenantId = "u2",
                tenantName = "Dimas Saputra",
                roomNumber = "Kamar 04",
                monthPeriod = "Oktober 2026",
                amount = 1500000.0,
                paymentDate = "01 Okt 2026",
                status = "Lunas",
                proofImageUrl = "proof_okt.jpg",
                note = "Transfer via Mandiri"
            )
        )
    )
    val riwayatPembayaran: StateFlow<List<PembayaranKos>> = _riwayatPembayaran.asStateFlow()

    fun kirimBuktiBayar(pembayaran: PembayaranKos) {
        _riwayatPembayaran.update { list -> listOf(pembayaran) + list }
        // Update month status
        _monthStatuses.update { list ->
            list.map {
                if (pembayaran.monthPeriod.startsWith(it.monthName, ignoreCase = true)) {
                    it.copy(isPaid = true)
                } else it
            }
        }
    }
}
