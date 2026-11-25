package com.example.kospace.ui.feature_anak_kos.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.ItemKulkas
import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.*
import kotlinx.coroutines.flow.*

class AnakKosBerandaViewModel(
    private val authRepository: AuthRepository = SupabaseAuthRepository,
    private val kerusakanRepository: KerusakanRepository = SupabaseKerusakanRepository,
    private val kulkasRepository: KulkasRepository = SupabaseKulkasRepository,
    private val pembayaranRepository: PembayaranRepository = SupabasePembayaranRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser

    val isCurrentMonthPaid: StateFlow<Boolean> = combine(
        pembayaranRepository.riwayatPembayaran,
        authRepository.currentUser
    ) { list, user ->
        if (user == null) false
        else {
            val userPayments = list.filter { it.tenantId == user.id }
            MonthPeriodHelper.isMonthPaidInPayments(
                payments = userPayments,
                monthNumber = MonthPeriodHelper.currentMonthNumber(),
                year = MonthPeriodHelper.currentYear()
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val myActiveReport: StateFlow<LaporanKerusakan?> = kerusakanRepository.laporanList
        .map { list -> list.firstOrNull { it.status != "Selesai" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val myFridgeItems: StateFlow<List<ItemKulkas>> = combine(
        kulkasRepository.items,
        authRepository.currentUser
    ) { items, user ->
        items.filter { it.ownerId == user?.id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
