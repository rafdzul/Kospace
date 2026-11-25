package com.example.kospace.ui.feature_auth

import androidx.lifecycle.ViewModel
import com.example.kospace.data.model.User
import com.example.kospace.data.repository.AuthRepository
import com.example.kospace.data.repository.SupabaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val authRepository: AuthRepository = SupabaseAuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser
    val authError: StateFlow<String?> = authRepository.authError

    private val _selectedRole = MutableStateFlow("ANAK_KOS")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun selectRole(role: String) {
        _selectedRole.value = role
        authRepository.clearError()
    }

    fun clearError() {
        authRepository.clearError()
    }

    fun login(emailOrPhone: String, password: String): Boolean {
        if (emailOrPhone.isNotBlank() && password.isNotBlank()) {
            return authRepository.login(emailOrPhone, _selectedRole.value)
        }
        return false
    }

    fun register(
        fullName: String,
        email: String,
        phone: String,
        roomNumber: String?,
        password: String
    ): Boolean {
        if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            authRepository.register(fullName, email, phone, roomNumber, _selectedRole.value)
            return true
        }
        return false
    }

    fun logout() {
        authRepository.logout()
    }
}
