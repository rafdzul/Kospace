package com.example.kospace.ui.feature_ibu_kos.manajemen

import androidx.lifecycle.ViewModel
import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AnakKosRepository
import com.example.kospace.data.repository.PembayaranRepository
import com.example.kospace.data.repository.SupabaseAnakKosRepository
import com.example.kospace.data.repository.SupabasePembayaranRepository
import kotlinx.coroutines.flow.StateFlow

class ManajemenPenghuniViewModel(
    private val anakKosRepository: AnakKosRepository = SupabaseAnakKosRepository,
    private val pembayaranRepository: PembayaranRepository = SupabasePembayaranRepository
) : ViewModel() {

    val penghuniAktif: StateFlow<List<User>> = anakKosRepository.penghuniAktif
    val pengajuanPending: StateFlow<List<User>> = anakKosRepository.pengajuanPending
    val monthStatuses: StateFlow<List<MonthStatus>> = pembayaranRepository.monthStatuses

    fun getPenghuniById(id: String): User? {
        return penghuniAktif.value.find { it.id == id }
    }

    fun terimaPendaftaran(userId: String) {
        anakKosRepository.terimaPendaftaran(userId)
    }

    fun tolakPendaftaran(userId: String) {
        anakKosRepository.tolakPendaftaran(userId)
    }

    fun keluarkanPenghuni(userId: String) {
        anakKosRepository.keluarkanPenghuni(userId)
    }
}
