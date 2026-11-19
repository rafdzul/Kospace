package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AnakKosRepository {
    val penghuniAktif: StateFlow<List<User>>
    val pengajuanPending: StateFlow<List<User>>
    fun terimaPendaftaran(userId: String)
    fun tolakPendaftaran(userId: String)
    fun keluarkanPenghuni(userId: String)
}
