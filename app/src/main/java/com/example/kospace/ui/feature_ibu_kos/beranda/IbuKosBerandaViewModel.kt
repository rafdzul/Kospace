package com.example.kospace.ui.feature_ibu_kos.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.*
import kotlinx.coroutines.flow.*

class IbuKosBerandaViewModel(
    private val authRepository: AuthRepository = SupabaseAuthRepository,
    private val kerusakanRepository: KerusakanRepository = SupabaseKerusakanRepository,
    private val anakKosRepository: AnakKosRepository = SupabaseAnakKosRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val kerusakanList: StateFlow<List<LaporanKerusakan>> = kerusakanRepository.laporanList
    val penghuniList: StateFlow<List<User>> = anakKosRepository.penghuniAktif
    val pendingList: StateFlow<List<User>> = anakKosRepository.pengajuanPending

    val pendingReportCount: StateFlow<Int> = kerusakanRepository.laporanList
        .map { list -> list.count { it.status == "Pending" || it.status == "Diproses" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun refresh() {
        anakKosRepository.refresh()
    }
}
