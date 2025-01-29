package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.core.manager.dto.ManagerDTO
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.core.manager.repository.ManagerRepository
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val managerRepository: ManagerRepository,
) {
    suspend fun create(email: String, name: String, encryptedPassword: String): ManagerDTO {
        return managerRepository.save(
            Manager(
                email = email,
                name = name,
                password = encryptedPassword,
            ),
        ).let { ManagerDTO(it) }
    }

    suspend fun findById(id: Long): ManagerDTO {
        return ManagerDTO(managerRepository.findById(id))
    }

    suspend fun findByEmail(email: String): ManagerDTO? {
        return managerRepository.findByEmail(email)?.let { ManagerDTO(it) }
    }

    suspend fun existsByEmail(email: String): Boolean {
        return managerRepository.findByEmail(email) != null
    }
}
