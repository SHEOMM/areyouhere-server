package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.crossConcern.error.ManagerNotExistsException
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ManagerFlowService(
    private val managerService: ManagerService,
    private val passwordEncoder: PasswordEncoder,
    private val sessionFactory: SessionFactory,
) {
    fun login(email: String, password: String): Uni<Boolean> {
        return sessionFactory.withSession{ session ->
            managerService.findByEmail(email, session)
        }.map {
            passwordEncoder.matches(password, it.password)
        }
    }
}
