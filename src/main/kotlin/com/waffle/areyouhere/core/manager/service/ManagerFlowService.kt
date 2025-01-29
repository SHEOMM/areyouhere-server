package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.crossConcern.error.ManagerNotExistsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ManagerFlowService(
    private val managerService: ManagerService,
    private val passwordEncoder: PasswordEncoder,
) {
    suspend fun login(email: String, password: String): Boolean {
        val foundManager = managerService.findByEmail(email) ?: throw ManagerNotExistsException
        if (passwordEncoder.matches(password, foundManager.password)) {
            return true
        }
        return false
    }
}
