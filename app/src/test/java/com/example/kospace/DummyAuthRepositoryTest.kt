package com.example.kospace

import com.example.kospace.data.repository.DummyAuthRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DummyAuthRepositoryTest {

    @Before
    fun setUp() {
        DummyAuthRepository.switchRole("ANAK_KOS")
    }

    @Test
    fun testLoginAsAnakKos() {
        val success = DummyAuthRepository.login("dimas@gmail.com", "ANAK_KOS")
        assertTrue(success)
        val user = DummyAuthRepository.currentUser.value
        assertNotNull(user)
        assertEquals("ANAK_KOS", user?.role)
        assertEquals("Dimas Saputra", user?.name)
    }

    @Test
    fun testLoginAsIbuKos() {
        val success = DummyAuthRepository.login("ibu.maryam@kospace.com", "IBU_KOS")
        assertTrue(success)
        val user = DummyAuthRepository.currentUser.value
        assertNotNull(user)
        assertEquals("IBU_KOS", user?.role)
        assertEquals("Hj. Maryam", user?.name)
    }

    @Test
    fun testLogout() {
        DummyAuthRepository.logout()
        assertNull(DummyAuthRepository.currentUser.value)
    }

    @Test
    fun testRegisterNewTenant() {
        DummyAuthRepository.register(
            name = "Test Tenant",
            email = "test@example.com",
            phone = "0811111111",
            roomNumber = "Kamar 99",
            role = "ANAK_KOS"
        )
        val user = DummyAuthRepository.currentUser.value
        assertNotNull(user)
        assertEquals("Test Tenant", user?.name)
        assertEquals("Kamar 99", user?.roomNumber)
        assertEquals("Pending", user?.statusPenghuni)
    }
}
