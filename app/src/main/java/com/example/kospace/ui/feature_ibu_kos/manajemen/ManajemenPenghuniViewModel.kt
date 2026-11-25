package com.example.kospace.ui.feature_ibu_kos.manajemen

import androidx.lifecycle.ViewModel
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AnakKosRepository
import com.example.kospace.data.repository.PembayaranRepository
import com.example.kospace.data.repository.SupabaseAnakKosRepository
import com.example.kospace.data.repository.SupabasePembayaranRepository
import kotlinx.coroutines.flow.StateFlow

class ManajemenPenghuniViewModel(
    private val anakKosRepository: AnakKosRepository = SupabaseAnakKosRepository,
    private val pembayaranRepository: PembayaranRepository = SupabasePembayaranRepository
) : ViewModel() {

    val penghuniAktif: StateFlow<List<User>> = anakKosRepository.penghuniAktif
    val pengajuanPending: StateFlow<List<User>> = anakKosRepository.pengajuanPending
    val monthStatuses: StateFlow<List<MonthStatus>> = pembayaranRepository.monthStatuses
    val riwayatPembayaran: StateFlow<List<PembayaranKos>> = pembayaranRepository.riwayatPembayaran

    fun refresh() {
        anakKosRepository.refresh()
        pembayaranRepository.refresh()
    }

    fun getPenghuniById(id: String): User? {
        return penghuniAktif.value.find { it.id == id }
    }

    fun terimaPendaftaran(userId: String) {
        anakKosRepository.terimaPendaftaran(userId)
    }

    fun tolakPendaftaran(userId: String) {
        anakKosRepository.tolakPendaftaran(userId)
    }

    fun keluarkanPenghuni(userId: String) {
        anakKosRepository.keluarkanPenghuni(userId)
    }

    /**
     * Returns list of distinct years that have at least one payment for the given tenant.
     * Always includes the current year. Sorted ascending.
     */
    fun getPaymentYearsForTenant(userId: String): List<Int> {
        val payments = riwayatPembayaran.value.filter { it.tenantId == userId }
        val paidYears = payments.mapNotNull { p ->
            p.monthPeriod.trim().split(" ").lastOrNull()?.toIntOrNull()
        }.toMutableSet()
        paidYears.add(MonthPeriodHelper.currentYear())
        return paidYears.sorted()
    }

    /**
     * Returns the payment status label for a tenant in a given year.
     * "Lunas" if all 12 months paid, "Tunggak" otherwise.
     */
    fun getStatusSewaForTenantYear(userId: String, year: Int): String {
        val payments = riwayatPembayaran.value.filter { it.tenantId == userId }
        val paidCount = (1..12).count { month ->
            MonthPeriodHelper.isMonthPaidInPayments(payments, month, year)
        }
        return if (paidCount >= 12) "Lunas" else if (paidCount > 0) "Aktif" else "Tunggak"
    }
}
