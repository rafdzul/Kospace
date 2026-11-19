package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import com.example.kospace.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object SupabaseAuthRepository : AuthRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val defaultAnakKos = User(
        id = "u2",
        name = "Dimas Saputra",
        email = "dimas@gmail.com",
        phone = "081234567890",
        role = "ANAK_KOS",
        roomNumber = "Kamar 04",
        kostName = "Kost Harmoni Utama",
        statusPenghuni = "Aktif"
    )

    private val defaultIbuKos = User(
        id = "u_ibu",
        name = "Hj. Maryam",
        email = "ibu.maryam@kospace.com",
        phone = "081122334455",
        role = "IBU_KOS",
        roomNumber = null,
        kostName = "Kost Harmoni Utama",
        statusPenghuni = "Aktif"
    )

    private val _currentUser = MutableStateFlow<User?>(defaultAnakKos)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override fun login(emailOrPhone: String, role: String): Boolean {
        return try {
            var foundUser: User? = null
            runBlocking(Dispatchers.IO) {
                try {
                    val users = SupabaseClient.client
                        .from("users")
                        .select {
                            filter {
                                eq("role", role)
                            }
                        }
                        .decodeList<User>()
                    foundUser = users.firstOrNull { it.email.equals(emailOrPhone, ignoreCase = true) || it.phone == emailOrPhone }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _currentUser.value = foundUser ?: if (role == "IBU_KOS") defaultIbuKos else defaultAnakKos
            true
        } catch (e: Exception) {
            _currentUser.value = if (role == "IBU_KOS") defaultIbuKos else defaultAnakKos
            true
        }
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

        scope.launch {
            try {
                SupabaseClient.client.from("users").insert(newUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun logout() {
        _currentUser.value = null
    }

    override fun switchRole(role: String) {
        _currentUser.value = if (role == "IBU_KOS") defaultIbuKos else defaultAnakKos
    }
}
