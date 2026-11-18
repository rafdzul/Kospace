package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DummyAuthRepository : AuthRepository {
    private val anakKosUser = User(
        id = "u2",
        name = "Dimas Saputra",
        email = "dimas@gmail.com",
        phone = "081234567890",
        role = "ANAK_KOS",
        roomNumber = "Kamar 04",
        kostName = "Kost Harmoni Utama",
        statusPenghuni = "Aktif"
    )

    private val ibuKosUser = User(
        id = "u_ibu",
        name = "Hj. Maryam",
        email = "ibu.maryam@kospace.com",
        phone = "081122334455",
        role = "IBU_KOS",
        roomNumber = null,
        kostName = "Kost Harmoni Utama",
        statusPenghuni = "Aktif"
    )

    private val _currentUser = MutableStateFlow<User?>(anakKosUser)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override fun login(emailOrPhone: String, role: String): Boolean {
        _currentUser.value = if (role == "IBU_KOS") ibuKosUser else anakKosUser
        return true
    }

    override fun register(name: String, email: String, phone: String, roomNumber: String?, role: String) {
        val newUser = User(
            id = "u_${System.currentTimeMillis()}",
            name = name,
            email = email,
            phone = phone,
            role = role,
            roomNumber = roomNumber,
            kostName = "Kost Harmoni Utama",
            statusPenghuni = if (role == "ANAK_KOS") "Pending" else "Aktif"
        )
        _currentUser.value = newUser
    }

    override fun logout() {
        _currentUser.value = null
    }

    override fun switchRole(role: String) {
        _currentUser.value = if (role == "IBU_KOS") ibuKosUser else anakKosUser
    }
}
