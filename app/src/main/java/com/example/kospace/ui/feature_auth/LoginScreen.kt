package com.example.kospace.ui.feature_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.ui.components.RoleSegmentedControl
import com.example.kospace.ui.theme.BackgroundLight
import com.example.kospace.ui.theme.PrimaryBlue
import com.example.kospace.ui.theme.TextPrimary
import com.example.kospace.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (String) -> Unit, // role
    onNavigateToRegister: (String) -> Unit
) {
    val selectedRole by viewModel.selectedRole.collectAsStateWithLifecycle()

    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kospace",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Text(
                text = "Aplikasi Manajemen Kos Terintegrasi",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Role Switcher
            RoleSegmentedControl(
                selectedRole = selectedRole,
                onRoleSelected = { viewModel.selectRole(it) },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Masuk Akun",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { 
                            emailOrPhone = it
                            errorMessage = null 
                        },
                        label = { Text("Email / Nomor HP") },
                        placeholder = { 
                            Text(if (selectedRole == "IBU_KOS") "ibu.maryam@kospace.com" else "dimas@gmail.com") 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            errorMessage = null 
                        },
                        label = { Text("Kata Sandi") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        }
                    )

                    // Helper isi akun demo untuk kemudahan pengujian
                    Text(
                        text = if (selectedRole == "IBU_KOS") "💡 Coba akun demo Ibu Kos (Hj. Maryam)" else "💡 Coba akun demo Anak Kos (Dimas)",
                        fontSize = 11.sp,
                        color = PrimaryBlue,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable {
                                if (selectedRole == "IBU_KOS") {
                                    emailOrPhone = "ibu.maryam@kospace.com"
                                    password = "password123"
                                } else {
                                    emailOrPhone = "dimas@gmail.com"
                                    password = "password123"
                                }
                                errorMessage = null
                            }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it }
                            )
                            Text(text = "Ingat Saya", fontSize = 12.sp, color = TextSecondary)
                        }

                        Text(
                            text = "Lupa Kata Sandi?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.clickable { }
                        )
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (emailOrPhone.isBlank() || password.isBlank()) {
                                errorMessage = "Email/Nomor HP dan kata sandi wajib diisi"
                                return@Button
                            }
                            val success = viewModel.login(emailOrPhone.trim(), password)
                            if (success) {
                                onLoginSuccess(selectedRole)
                            } else {
                                errorMessage = "Akun tidak ditemukan atau tidak sesuai peran ${if (selectedRole == "IBU_KOS") "Ibu Kos" else "Anak Kos"}."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Masuk Sebagai ${if (selectedRole == "IBU_KOS") "Ibu Kos" else "Anak Kos"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(text = "Belum punya akun? ", color = TextSecondary, fontSize = 14.sp)
                Text(
                    text = "Daftar Sekarang",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToRegister(selectedRole) }
                )
            }
        }
    }
}
