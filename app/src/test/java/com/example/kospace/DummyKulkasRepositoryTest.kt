package com.example.kospace

import com.example.kospace.data.model.ItemKulkas
import com.example.kospace.data.repository.DummyKulkasRepository
import org.junit.Assert.*
import org.junit.Test

class DummyKulkasRepositoryTest {

    @Test
    fun testTambahDanHapusItemKulkas() {
        val testItem = ItemKulkas(
            id = "test_kulkas_1",
            ownerId = "u_test",
            ownerName = "Tester",
            roomNumber = "Kamar 01",
            itemName = "Yogurt Blueberry",
            quantity = 2,
            unit = "Cup",
            expirationDate = "25 Nov 2026",
            addedDate = "Hari ini"
        )
        DummyKulkasRepository.tambahItem(testItem)
        assertTrue(DummyKulkasRepository.items.value.any { it.id == "test_kulkas_1" })

        DummyKulkasRepository.hapusItem("test_kulkas_1")
        assertFalse(DummyKulkasRepository.items.value.any { it.id == "test_kulkas_1" })
    }
}
