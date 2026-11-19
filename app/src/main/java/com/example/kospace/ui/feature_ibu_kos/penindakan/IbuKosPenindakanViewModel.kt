package com.example.kospace.ui.feature_ibu_kos.penindakan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.repository.KerusakanRepository
import com.example.kospace.data.repository.SupabaseKerusakanRepository
import kotlinx.coroutines.flow.*

class IbuKosPenindakanViewModel(
    private val kerusakanRepository: KerusakanRepository = SupabaseKerusakanRepository
) : ViewModel() {

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

    fun updateStatusPenanganan(id: String, statusBaru: String, catatan: String?) {
        kerusakanRepository.updateStatusPenanganan(id, statusBaru, catatan)
    }
}
