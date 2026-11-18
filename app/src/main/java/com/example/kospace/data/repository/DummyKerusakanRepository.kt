package com.example.kospace.data.repository

import com.example.kospace.data.model.LaporanKerusakan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object DummyKerusakanRepository : KerusakanRepository {
    private val _laporanList = MutableStateFlow(
        listOf(
            LaporanKerusakan(
                id = "1",
                ticketCode = "REP-001",
                tenantId = "u1",
                tenantName = "Budi Santoso",
                roomNumber = "Kamar 02 • Lantai 1",
                title = "Kran Wastafel Patah & Bocor",
                categoryLocation = "Kamar Mandi Lt. 2",
                description = "Keran wastafel patah dan air mengalir kencang tidak bisa ditutup rapat...",
                status = "Pending",
                reportDate = "Hari ini, 09:15"
            ),
            LaporanKerusakan(
                id = "2",
                ticketCode = "REP-002",
                tenantId = "u2",
                tenantName = "Dimas Saputra",
                roomNumber = "Kamar 04",
                title = "AC Kamar Tidak Dingin",
                categoryLocation = "Kamar 04 - AC",
                description = "Hanya keluar angin dan meneteskan air pada bagian casing bawah",
                status = "Diproses",
                reportDate = "Dilaporkan kemarin",
                technicianInfo = "Teknisi Pak Joko (Dijadwalkan 14:00)"
            ),
            LaporanKerusakan(
                id = "3",
                ticketCode = "REP-003",
                tenantId = "u3",
                tenantName = "Rian Hidayat",
                roomNumber = "Kamar 07",
                title = "Stopkontak Meja Mati",
                categoryLocation = "Kamar 07 - Listrik",
                description = "Stopkontak diganti dengan unit baru oleh teknisi",
                status = "Selesai",
                reportDate = "22 Okt 2026",
                actionNote = "Sudah diganti stopkontak baru merk Broco"
            )
        )
    )
    override val laporanList: StateFlow<List<LaporanKerusakan>> = _laporanList.asStateFlow()

    override fun updateStatusPenanganan(id: String, statusBaru: String, catatan: String?) {
        _laporanList.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(status = statusBaru, actionNote = catatan ?: item.actionNote)
                } else item
            }
        }
    }

    override fun tambahLaporan(laporan: LaporanKerusakan) {
        _laporanList.update { list -> listOf(laporan) + list }
    }

    override fun batalkanLaporan(id: String) {
        _laporanList.update { list -> list.filterNot { it.id == id } }
    }
}
