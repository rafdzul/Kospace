package com.example.kospace.data.repository

import com.example.kospace.data.model.ItemKulkas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object DummyKulkasRepository {
    private val _items = MutableStateFlow(
        listOf(
            ItemKulkas(
                id = "k1",
                ownerId = "u2",
                ownerName = "Dimas Saputra",
                roomNumber = "Kamar 04",
                itemName = "Susu UHT Full Cream",
                quantity = 2,
                unit = "Botol",
                expirationDate = "12 Nov 2026",
                note = "Jangan diminum ya guys!",
                addedDate = "01 Nov 2026"
            ),
            ItemKulkas(
                id = "k2",
                ownerId = "u3",
                ownerName = "Rian Hidayat",
                roomNumber = "Kamar 07",
                itemName = "Buah Apel Fuji",
                quantity = 4,
                unit = "Pcs",
                expirationDate = "10 Nov 2026",
                note = "Label: Rian 07",
                addedDate = "02 Nov 2026"
            ),
            ItemKulkas(
                id = "k3",
                ownerId = "u2",
                ownerName = "Dimas Saputra",
                roomNumber = "Kamar 04",
                itemName = "Nasi Goreng Spesial",
                quantity = 1,
                unit = "Porsi",
                expirationDate = "05 Nov 2026",
                note = "Silakan dimakan kalau mau",
                addedDate = "03 Nov 2026"
            ),
            ItemKulkas(
                id = "k4",
                ownerId = "u1",
                ownerName = "Budi Santoso",
                roomNumber = "Kamar 02",
                itemName = "Keju Slice",
                quantity = 1,
                unit = "Pack",
                expirationDate = "20 Nov 2026",
                note = null,
                addedDate = "28 Okt 2026"
            )
        )
    )
    val items: StateFlow<List<ItemKulkas>> = _items.asStateFlow()

    fun tambahItem(item: ItemKulkas) {
        _items.update { list -> listOf(item) + list }
    }

    fun hapusItem(id: String) {
        _items.update { list -> list.filterNot { it.id == id } }
    }
}
