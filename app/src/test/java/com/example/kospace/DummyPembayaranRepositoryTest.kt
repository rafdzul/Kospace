package com.example.kospace

import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.data.repository.DummyPembayaranRepository
import org.junit.Assert.*
import org.junit.Test

class DummyPembayaranRepositoryTest {

    @Test
    fun testKirimBuktiBayar() {
        val initialCount = DummyPembayaranRepository.riwayatPembayaran.value.size
        val payment = PembayaranKos(
            id = "test_pay_1",
            tenantId = "u2",
            tenantName = "Dimas Saputra",
            roomNumber = "Kamar 04",
            monthPeriod = "Desember 2026",
            amount = 1500000.0,
            paymentDate = "01 Des 2026",
            status = "Lunas"
        )
        DummyPembayaranRepository.kirimBuktiBayar(payment)

        val updatedPayments = DummyPembayaranRepository.riwayatPembayaran.value
        assertEquals(initialCount + 1, updatedPayments.size)
        assertEquals("test_pay_1", updatedPayments.first().id)

        val decStatus = DummyPembayaranRepository.monthStatuses.value.find { it.monthNumber == 12 }
        assertNotNull(decStatus)
        assertTrue(decStatus?.isPaid == true)
    }
}
