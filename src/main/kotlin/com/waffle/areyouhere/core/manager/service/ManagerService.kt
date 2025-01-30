package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.core.manager.dto.ManagerDTO
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.core.manager.repository.ManagerRepository
import io.smallrye.mutiny.Uni
import org.hibernate.reactive.mutiny.Mutiny.Session
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

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

    fun findByEmail(email: String): Mono<Manager> {
        return managerRepository.findByEmail(email)
    }

    fun findByEmail(email: String, session: Session): Uni<Manager>{
        return managerRepository.findByEmail(email, session)
    }

    suspend fun existsByEmail(email: String): Boolean {
        return managerRepository.findByEmail(email) != null
    }
}
