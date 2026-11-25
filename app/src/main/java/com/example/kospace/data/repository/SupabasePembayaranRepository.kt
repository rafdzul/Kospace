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

    private val _monthStatuses = MutableStateFlow(
        listOf(
            MonthStatus("Jan", 1, false),
            MonthStatus("Feb", 2, false),
            MonthStatus("Mar", 3, false),
            MonthStatus("Apr", 4, false),
            MonthStatus("Mei", 5, false),
            MonthStatus("Jun", 6, false),
            MonthStatus("Jul", 7, false),
            MonthStatus("Agu", 8, false),
            MonthStatus("Sep", 9, false),
            MonthStatus("Okt", 10, false),
            MonthStatus("Nov", 11, false),
            MonthStatus("Des", 12, false)
        )
    )
    override val monthStatuses: StateFlow<List<MonthStatus>> = _monthStatuses.asStateFlow()

    private val _riwayatPembayaran = MutableStateFlow<List<PembayaranKos>>(emptyList())
    override val riwayatPembayaran: StateFlow<List<PembayaranKos>> = _riwayatPembayaran.asStateFlow()

    init {
        refresh()
    }

    override fun refresh() {
        scope.launch {
            try {
                val list = SupabaseClient.client
                    .from("pembayaran_kos")
                    .select()
                    .decodeList<PembayaranKos>()
                _riwayatPembayaran.value = list
                _monthStatuses.update { months ->
                    months.map { m ->
                        val isPaidInDb = list.any { p ->
                            p.monthPeriod.startsWith(m.monthName, ignoreCase = true) &&
                                p.status.equals("Lunas", ignoreCase = true)
                        }
                        m.copy(isPaid = isPaidInDb)
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
