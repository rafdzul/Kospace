package com.example.kospace.data.repository

import com.example.kospace.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    val authError: StateFlow<String?>
    fun clearError()
    fun login(emailOrPhone: String, role: String): Boolean
    fun register(name: String, email: String, phone: String, roomNumber: String?, role: String)
    fun logout()
    fun switchRole(role: String)
}
