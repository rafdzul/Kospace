package com.example.kospace.data.repository

import com.example.kospace.data.model.ItemKulkas
import com.example.kospace.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object SupabaseKulkasRepository : KulkasRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _items = MutableStateFlow<List<ItemKulkas>>(emptyList())
    override val items: StateFlow<List<ItemKulkas>> = _items.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        scope.launch {
            try {
                val result = SupabaseClient.client
                    .from("kulkas")
                    .select()
                    .decodeList<ItemKulkas>()
                if (result.isNotEmpty()) {
                    _items.value = result
                } else if (_items.value.isEmpty()) {
                    // Pakai fallback dummy jika di database masih kosong
                    _items.value = DummyKulkasRepository.items.value
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (_items.value.isEmpty()) {
                    _items.value = DummyKulkasRepository.items.value
                }
            }
        }
    }

    override fun tambahItem(item: ItemKulkas) {
        _items.value = listOf(item) + _items.value
        scope.launch {
            try {
                SupabaseClient.client.from("kulkas").insert(item)
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun hapusItem(id: String) {
        _items.value = _items.value.filterNot { it.id == id }
        scope.launch {
            try {
                SupabaseClient.client.from("kulkas").delete {
                    filter { eq("id", id) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
