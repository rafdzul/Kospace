package com.example.kospace.ui.feature_anak_kos.pembayaran

import androidx.lifecycle.ViewModel
import com.example.kospace.data.model.MonthStatus
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AuthRepository
import com.example.kospace.data.repository.PembayaranRepository
import com.example.kospace.data.repository.SupabaseAuthRepository
import com.example.kospace.data.repository.SupabasePembayaranRepository
import kotlinx.coroutines.flow.StateFlow

class PembayaranViewModel(
    private val pembayaranRepository: PembayaranRepository = SupabasePembayaranRepository,
    private val authRepository: AuthRepository = SupabaseAuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val monthStatuses: StateFlow<List<MonthStatus>> = pembayaranRepository.monthStatuses
    val riwayatPembayaran: StateFlow<List<PembayaranKos>> = pembayaranRepository.riwayatPembayaran

    fun kirimBuktiBayar(
        monthPeriod: String,
        amount: Double,
        note: String?,
        proofImageUrl: String? = "proof_sample.jpg"
    ) {
        val user = currentUser.value
        val payment = PembayaranKos(
            id = "p_${System.currentTimeMillis()}",
            tenantId = user?.id ?: "u2",
            tenantName = user?.name ?: "Dimas Saputra",
            roomNumber = user?.roomNumber ?: "Kamar 04",
            monthPeriod = monthPeriod,
            amount = amount,
            paymentDate = "Hari ini",
            status = "Lunas",
            proofImageUrl = proofImageUrl,
            note = note?.ifBlank { null }
        )
        pembayaranRepository.kirimBuktiBayar(payment)
    }
}
