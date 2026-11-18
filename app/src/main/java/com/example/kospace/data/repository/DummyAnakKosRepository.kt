package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object DummyAnakKosRepository {
    private val _penghuniAktif = MutableStateFlow(
        listOf(
            User(
                id = "u1",
                name = "Budi Santoso",
                email = "budi@gmail.com",
                phone = "081299887766",
                role = "ANAK_KOS",
                roomNumber = "Kamar 01",
                statusPenghuni = "Aktif"
            ),
            User(
                id = "u2",
                name = "Dimas Saputra",
                email = "dimas@gmail.com",
                phone = "081234567890",
                role = "ANAK_KOS",
                roomNumber = "Kamar 04",
                statusPenghuni = "Aktif"
            ),
            User(
                id = "u3",
                name = "Rian Hidayat",
                email = "rian@gmail.com",
                phone = "085711223344",
                role = "ANAK_KOS",
                roomNumber = "Kamar 07",
                statusPenghuni = "Aktif"
            ),
            User(
                id = "u4",
                name = "Ahmad Dani",
                email = "dani@gmail.com",
                phone = "081388776655",
                role = "ANAK_KOS",
                roomNumber = "Kamar 09",
                statusPenghuni = "Aktif"
            )
        )
    )
    val penghuniAktif: StateFlow<List<User>> = _penghuniAktif.asStateFlow()

    private val _pengajuanPending = MutableStateFlow(
        listOf(
            User(
                id = "u_pending1",
                name = "Zahra Amalia",
                email = "zahra@gmail.com",
                phone = "081900112233",
                role = "ANAK_KOS",
                roomNumber = "Kamar 03",
                statusPenghuni = "Pending"
            ),
            User(
                id = "u_pending2",
                name = "Rafi Alamsyah",
                email = "rafi@gmail.com",
                phone = "082144556677",
                role = "ANAK_KOS",
                roomNumber = "Kamar 08",
                statusPenghuni = "Pending"
            )
        )
    )
    val pengajuanPending: StateFlow<List<User>> = _pengajuanPending.asStateFlow()

    fun terimaPendaftaran(userId: String) {
        val target = _pengajuanPending.value.find { it.id == userId }
        if (target != null) {
            _pengajuanPending.update { list -> list.filterNot { it.id == userId } }
            _penghuniAktif.update { list -> list + target.copy(statusPenghuni = "Aktif") }
        }
    }

    fun tolakPendaftaran(userId: String) {
        _pengajuanPending.update { list -> list.filterNot { it.id == userId } }
    }

    fun keluarkanPenghuni(userId: String) {
        _penghuniAktif.update { list -> list.filterNot { it.id == userId } }
    }
}
