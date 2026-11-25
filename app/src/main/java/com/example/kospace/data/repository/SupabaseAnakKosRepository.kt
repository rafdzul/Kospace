package com.example.kospace.data.repository

import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.data.model.PembayaranKos
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
import java.time.LocalDate

object SupabaseAnakKosRepository : AnakKosRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _penghuniAktif = MutableStateFlow<List<User>>(emptyList())
    override val penghuniAktif: StateFlow<List<User>> = _penghuniAktif.asStateFlow()

    private val _pengajuanPending = MutableStateFlow<List<User>>(emptyList())
    override val pengajuanPending: StateFlow<List<User>> = _pengajuanPending.asStateFlow()

    init {
        refresh()
    }

    override fun refresh() {
        scope.launch {
            try {
                val allUsers = SupabaseClient.client
                    .from("users")
                    .select {
                        filter { eq("role", "ANAK_KOS") }
                    }
                    .decodeList<User>()

                _penghuniAktif.value = allUsers.filter { it.statusPenghuni.equals("Aktif", ignoreCase = true) }
                _pengajuanPending.value = allUsers.filter { it.statusPenghuni.equals("Pending", ignoreCase = true) }
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
                if (target != null) {
                    val now = LocalDate.now()
                    val currentMonthLabel = MonthPeriodHelper.fullMonthYear(now.monthValue, now.year)
                    val alreadyPaid = SupabasePembayaranRepository.riwayatPembayaran.value.any { p ->
                        p.tenantId == target.id &&
                            MonthPeriodHelper.isMonthPaidInPayments(listOf(p), now.monthValue, now.year)
                    }
                    if (!alreadyPaid) {
                        val initialPayment = PembayaranKos(
                            id = "p_init_${target.id}_${now.year}_${now.monthValue}",
                            tenantId = target.id,
                            tenantName = target.name,
                            roomNumber = target.roomNumber ?: "-",
                            monthPeriod = currentMonthLabel,
                            amount = 1500000.0,
                            paymentDate = MonthPeriodHelper.shortDateLabel(now),
                            status = "Lunas",
                            proofImageUrl = "pembayaran_awal_masuk_kos.jpg",
                            note = "Pembayaran awal saat pendaftaran disetujui"
                        )
                        SupabasePembayaranRepository.kirimBuktiBayar(initialPayment)
                    }
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
