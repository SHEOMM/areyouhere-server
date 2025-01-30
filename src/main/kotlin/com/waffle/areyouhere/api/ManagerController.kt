package com.waffle.areyouhere.api

import com.waffle.areyouhere.api.dto.LoginRequestDTO
import com.waffle.areyouhere.core.manager.service.ManagerFlowService
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class ManagerController(
    private val managerFlowService: ManagerFlowService,
) {
    @PostMapping("login")
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDTO): Mono<ResponseEntity<HttpStatus>> {
        return managerFlowService.login(loginRequestDto.email, loginRequestDto.password)
            .convert().with(UniReactorConverters.toMono())
            .map {
                return@map if (it) ResponseEntity.status(HttpStatus.OK).build()
                else ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
    }
}
