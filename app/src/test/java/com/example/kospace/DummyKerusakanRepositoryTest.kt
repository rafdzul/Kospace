package com.example.kospace

import com.example.kospace.data.model.LaporanKerusakan
import com.example.kospace.data.repository.DummyKerusakanRepository
import org.junit.Assert.*
import org.junit.Test

class DummyKerusakanRepositoryTest {

    @Test
    fun testInitialListNotEmpty() {
        val list = DummyKerusakanRepository.laporanList.value
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun testTambahLaporan() {
        val initialCount = DummyKerusakanRepository.laporanList.value.size
        val newReport = LaporanKerusakan(
            id = "test_rep_1",
            ticketCode = "REP-TEST",
            tenantId = "u_test",
            tenantName = "Tester",
            roomNumber = "Kamar 99",
            title = "Lampu Redup",
            categoryLocation = "Kamar 99",
            description = "Lampu berkedip-kedip",
            status = "Pending",
            reportDate = "Hari ini"
        )
        DummyKerusakanRepository.tambahLaporan(newReport)

        val updatedList = DummyKerusakanRepository.laporanList.value
        assertEquals(initialCount + 1, updatedList.size)
        assertEquals("test_rep_1", updatedList.first().id)
    }

    @Test
    fun testUpdateStatusPenanganan() {
        val testId = "test_rep_status"
        val report = LaporanKerusakan(
            id = testId,
            ticketCode = "REP-TEST2",
            tenantId = "u_test",
            tenantName = "Tester",
            roomNumber = "Kamar 99",
            title = "Pintu Rusak",
            categoryLocation = "Kamar 99",
            description = "Engsel lepas",
            status = "Pending",
            reportDate = "Hari ini"
        )
        DummyKerusakanRepository.tambahLaporan(report)
        DummyKerusakanRepository.updateStatusPenanganan(testId, "Selesai", "Sudah diperbaiki tukang")

        val target = DummyKerusakanRepository.laporanList.value.find { it.id == testId }
        assertNotNull(target)
        assertEquals("Selesai", target?.status)
        assertEquals("Sudah diperbaiki tukang", target?.actionNote)
    }

    @Test
    fun testBatalkanLaporan() {
        val testId = "test_cancel"
        val report = LaporanKerusakan(
            id = testId,
            ticketCode = "REP-CANCEL",
            tenantId = "u_test",
            tenantName = "Tester",
            roomNumber = "Kamar 99",
            title = "Test Cancel",
            categoryLocation = "Kamar 99",
            description = "Test cancel description",
            status = "Pending",
            reportDate = "Hari ini"
        )
        DummyKerusakanRepository.tambahLaporan(report)
        assertTrue(DummyKerusakanRepository.laporanList.value.any { it.id == testId })

        DummyKerusakanRepository.batalkanLaporan(testId)
        assertFalse(DummyKerusakanRepository.laporanList.value.any { it.id == testId })
    }
}
