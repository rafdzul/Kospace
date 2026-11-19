package com.example.kospace.ui.feature_anak_kos.kulkas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.ItemKulkas
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AuthRepository
import com.example.kospace.data.repository.KulkasRepository
import com.example.kospace.data.repository.SupabaseAuthRepository
import com.example.kospace.data.repository.SupabaseKulkasRepository
import kotlinx.coroutines.flow.*

class KulkasViewModel(
    private val kulkasRepository: KulkasRepository = SupabaseKulkasRepository,
    private val authRepository: AuthRepository = SupabaseAuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser

    private val _selectedTab = MutableStateFlow(0) // 0: Semua Barang, 1: Barang Saya
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    val filteredItems: StateFlow<List<ItemKulkas>> = combine(
        kulkasRepository.items,
        _selectedTab,
        authRepository.currentUser
    ) { items, tab, user ->
        if (tab == 1) {
            items.filter { it.ownerId == user?.id }
        } else {
            items
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allItemsCount: StateFlow<Int> = kulkasRepository.items
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val myItemCount: StateFlow<Int> = combine(
        kulkasRepository.items,
        authRepository.currentUser
    ) { items, user ->
        items.count { it.ownerId == user?.id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun hapusItem(id: String) {
        kulkasRepository.hapusItem(id)
    }

    fun tambahItem(
        itemName: String,
        quantity: Int,
        unit: String,
        expirationDate: String,
        note: String?
    ) {
        val user = currentUser.value
        val newItem = ItemKulkas(
            id = "k_${System.currentTimeMillis()}",
            ownerId = user?.id ?: "u2",
            ownerName = user?.name ?: "Dimas Saputra",
            roomNumber = user?.roomNumber ?: "Kamar 04",
            itemName = itemName,
            quantity = quantity,
            unit = unit,
            expirationDate = expirationDate,
            note = note?.ifBlank { null },
            addedDate = "Hari ini"
        )
        kulkasRepository.tambahItem(newItem)
    }
}
