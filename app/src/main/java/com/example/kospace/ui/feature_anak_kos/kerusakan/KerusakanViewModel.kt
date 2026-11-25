package com.example.kospace.ui.feature_anak_kos.kerusakan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AuthRepository
import com.example.kospace.data.repository.KerusakanRepository
import com.example.kospace.data.repository.SupabaseAuthRepository
import com.example.kospace.data.repository.SupabaseKerusakanRepository
import kotlinx.coroutines.flow.*

class KerusakanViewModel(
    private val kerusakanRepository: KerusakanRepository = SupabaseKerusakanRepository,
    private val authRepository: AuthRepository = SupabaseAuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val laporanList: StateFlow<List<LaporanKerusakan>> = kerusakanRepository.laporanList

    private val _selectedFilter = MutableStateFlow("Semua")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    val filteredList: StateFlow<List<LaporanKerusakan>> = combine(
        kerusakanRepository.laporanList,
        _selectedFilter
    ) { list, filter ->
        when (filter) {
            "Diproses" -> list.filter { it.status == "Diproses" || it.status == "Pending" }
            "Selesai" -> list.filter { it.status == "Selesai" }
            else -> list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun getLaporanById(id: String): LaporanKerusakan? {
        return laporanList.value.find { it.id == id }
    }

    fun tambahLaporan(
        title: String,
        locationCategory: String,
        description: String,
        imageUrl: String? = null
    ) {
        val user = currentUser.value?: return

        if (user.name.isBlank()) return

        val report = LaporanKerusakan(
            id = "${System.currentTimeMillis()}",
            ticketCode = "REP-00${(4..99).random()}",
            tenantId = user.id,
            tenantName = user.name,
            roomNumber = user?.roomNumber ?: "Kamar 04",
            title = title,
            categoryLocation = locationCategory,
            description = description,
            imageUrl = imageUrl ?: "damage_sample.jpg",
            status = "Pending",
            reportDate = "Hari ini, Baru saja"
        )
        kerusakanRepository.tambahLaporan(report)
    }

    fun batalkanLaporan(id: String) {
        kerusakanRepository.batalkanLaporan(id)
    }
}
