package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import com.example.kospace.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SupabaseAnakKosRepository : AnakKosRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _penghuniAktif = MutableStateFlow(DummyAnakKosRepository.penghuniAktif.value)
    override val penghuniAktif: StateFlow<List<User>> = _penghuniAktif.asStateFlow()

    private val _pengajuanPending = MutableStateFlow(DummyAnakKosRepository.pengajuanPending.value)
    override val pengajuanPending: StateFlow<List<User>> = _pengajuanPending.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        scope.launch {
            try {
                val allUsers = SupabaseClient.client
                    .from("users")
                    .select {
                        filter { eq("role", "ANAK_KOS") }
                    }
                    .decodeList<User>()

                if (allUsers.isNotEmpty()) {
                    _penghuniAktif.value = allUsers.filter { it.statusPenghuni == "Aktif" }
                    _pengajuanPending.value = allUsers.filter { it.statusPenghuni == "Pending" }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun terimaPendaftaran(userId: String) {
        val target = _pengajuanPending.value.find { it.id == userId }
        if (target != null) {
            _pengajuanPending.update { list -> list.filterNot { it.id == userId } }
            _penghuniAktif.update { list -> list + target.copy(statusPenghuni = "Aktif") }
        }
        scope.launch {
            try {
                SupabaseClient.client.from("users").update(
                    mapOf("status_penghuni" to "Aktif")
                ) {
                    filter { eq("id", userId) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun tolakPendaftaran(userId: String) {
        _pengajuanPending.update { list -> list.filterNot { it.id == userId } }
        scope.launch {
            try {
                SupabaseClient.client.from("users").delete {
                    filter { eq("id", userId) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun keluarkanPenghuni(userId: String) {
        _penghuniAktif.update { list -> list.filterNot { it.id == userId } }
        scope.launch {
            try {
                SupabaseClient.client.from("users").delete {
                    filter { eq("id", userId) }
                }
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
