package com.example.kospace.data.repository

import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SupabaseKerusakanRepository : KerusakanRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _laporanList = MutableStateFlow<List<LaporanKerusakan>>(emptyList())
    override val laporanList: StateFlow<List<LaporanKerusakan>> = _laporanList.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        scope.launch {
            try {
                val result = SupabaseClient.client
                    .from("laporan_kerusakan")
                    .select()
                    .decodeList<LaporanKerusakan>()
                _laporanList.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun updateStatusPenanganan(id: String, statusBaru: String, catatan: String?) {
        _laporanList.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(status = statusBaru, actionNote = catatan ?: item.actionNote)
                } else item
            }
        }
        scope.launch {
            try {
                SupabaseClient.client.from("laporan_kerusakan").update(
                    mapOf(
                        "status" to statusBaru,
                        "action_note" to (catatan ?: "")
                    )
                ) {
                    filter { eq("id", id) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun tambahLaporan(laporan: LaporanKerusakan) {
        _laporanList.update { listOf(laporan) + it }
        scope.launch {
            try {
                SupabaseClient.client.from("laporan_kerusakan").insert(laporan)
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun batalkanLaporan(id: String) {
        _laporanList.update { it.filterNot { item -> item.id == id } }
        scope.launch {
            try {
                SupabaseClient.client.from("laporan_kerusakan").delete {
                    filter { eq("id", id) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
