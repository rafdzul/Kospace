package com.example.kospace.data.repository

import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SupabasePembayaranRepository : PembayaranRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _monthStatuses = MutableStateFlow(DummyPembayaranRepository.monthStatuses.value)
    override val monthStatuses: StateFlow<List<MonthStatus>> = _monthStatuses.asStateFlow()

    private val _riwayatPembayaran = MutableStateFlow(DummyPembayaranRepository.riwayatPembayaran.value)
    override val riwayatPembayaran: StateFlow<List<PembayaranKos>> = _riwayatPembayaran.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        scope.launch {
            try {
                val list = SupabaseClient.client
                    .from("pembayaran_kos")
                    .select()
                    .decodeList<PembayaranKos>()
                if (list.isNotEmpty()) {
                    _riwayatPembayaran.value = list
                    _monthStatuses.update { months ->
                        months.map { m ->
                            val isPaidInDb = list.any { p ->
                                p.monthPeriod.startsWith(m.monthName, ignoreCase = true) && p.status == "Lunas"
                            }
                            if (isPaidInDb) m.copy(isPaid = true) else m
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun kirimBuktiBayar(pembayaran: PembayaranKos) {
        _riwayatPembayaran.update { listOf(pembayaran) + it }
        _monthStatuses.update { list ->
            list.map {
                if (pembayaran.monthPeriod.startsWith(it.monthName, ignoreCase = true)) {
                    it.copy(isPaid = true)
                } else it
            }
        }
        scope.launch {
            try {
                SupabaseClient.client.from("pembayaran_kos").insert(pembayaran)
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
