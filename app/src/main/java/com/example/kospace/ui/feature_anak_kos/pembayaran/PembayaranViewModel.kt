package com.example.kospace.ui.feature_anak_kos.pembayaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AuthRepository
import com.example.kospace.data.repository.PembayaranRepository
import com.example.kospace.data.repository.SupabaseAuthRepository
import com.example.kospace.data.repository.SupabasePembayaranRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PembayaranViewModel(
    private val pembayaranRepository: PembayaranRepository = SupabasePembayaranRepository,
    private val authRepository: AuthRepository = SupabaseAuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val currentYear: Int = MonthPeriodHelper.currentYear()

    val riwayatPembayaran: StateFlow<List<PembayaranKos>> = combine(
        pembayaranRepository.riwayatPembayaran,
        authRepository.currentUser
    ) { list, user ->
        if (user == null) emptyList()
        else list.filter { it.tenantId == user.id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthStatuses: StateFlow<List<MonthStatus>> = combine(
        pembayaranRepository.monthStatuses,
        riwayatPembayaran
    ) { baseMonths, userPayments ->
        baseMonths.map { m ->
            m.copy(
                isPaid = MonthPeriodHelper.isMonthPaidInPayments(
                    payments = userPayments,
                    monthNumber = m.monthNumber,
                    year = currentYear
                )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), pembayaranRepository.monthStatuses.value)

    fun refresh() {
        pembayaranRepository.refresh()
    }

    fun isMonthYearPaid(monthNumber: Int, year: Int): Boolean {
        return MonthPeriodHelper.isMonthPaidInPayments(riwayatPembayaran.value, monthNumber, year)
    }

    fun kirimBuktiBayar(
        monthPeriods: List<String>,
        totalAmount: Double,
        note: String?,
        proofImageUrl: String? = "proof_sample.jpg"
    ) {
        val user = currentUser.value ?: return
        if (monthPeriods.isEmpty()) return

        val nowMillis = System.currentTimeMillis()
        val amountPerMonth = totalAmount / monthPeriods.size
        val dateLabel = MonthPeriodHelper.shortDateLabel()
        val cleanNote = note?.ifBlank { null }

        monthPeriods.forEachIndexed { index, period ->
            val payment = PembayaranKos(
                id = "p_${nowMillis}_$index",
                tenantId = user.id,
                tenantName = user.name,
                roomNumber = user.roomNumber ?: "-",
                monthPeriod = period,
                amount = amountPerMonth,
                paymentDate = dateLabel,
                status = "Lunas",
                proofImageUrl = proofImageUrl,
                note = cleanNote
            )
            pembayaranRepository.kirimBuktiBayar(payment)
        }
    }
}
