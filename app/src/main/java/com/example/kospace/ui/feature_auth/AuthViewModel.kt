package com.example.kospace.ui.feature_auth

import androidx.lifecycle.ViewModel
import com.example.kospace.data.repository.DummyAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _selectedRole = MutableStateFlow("ANAK_KOS")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun selectRole(role: String) {
        _selectedRole.value = role
        DummyAuthRepository.switchRole(role)
    }

    fun login(emailOrPhone: String, password: String): Boolean {
        if (emailOrPhone.isNotBlank() && password.isNotBlank()) {
            return DummyAuthRepository.login(emailOrPhone, _selectedRole.value)
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
            DummyAuthRepository.register(fullName, email, phone, roomNumber, _selectedRole.value)
            return true
        }
        return false
    }
}
