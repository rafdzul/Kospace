package com.example.kospace.data.repository

import com.example.kospace.data.model.ItemKulkas
import kotlinx.coroutines.flow.StateFlow

interface KulkasRepository {
    val items: StateFlow<List<ItemKulkas>>
    fun tambahItem(item: ItemKulkas)
    fun hapusItem(id: String)
}
