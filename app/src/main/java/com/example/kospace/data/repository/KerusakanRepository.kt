package com.example.kospace.data.repository

import com.example.kospace.data.model.LaporanKerusakan
import kotlinx.coroutines.flow.StateFlow

interface KerusakanRepository {
    val laporanList: StateFlow<List<LaporanKerusakan>>
    fun updateStatusPenanganan(id: String, statusBaru: String, catatan: String?)
    fun tambahLaporan(laporan: LaporanKerusakan)
    fun batalkanLaporan(id: String)
}
