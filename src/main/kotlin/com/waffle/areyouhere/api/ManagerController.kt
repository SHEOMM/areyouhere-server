package com.waffle.areyouhere.api

import com.waffle.areyouhere.api.dto.LoginRequestDTO
import com.waffle.areyouhere.core.manager.service.ManagerFlowService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class ManagerController(
    private val managerFlowService: ManagerFlowService,
) {
    @PostMapping("login")
    suspend fun login(@RequestBody @Valid loginRequestDto: LoginRequestDTO): ResponseEntity<HttpStatus> {
        if (managerFlowService.login(loginRequestDto.email, loginRequestDto.password)) {
            return ResponseEntity.status(HttpStatus.OK).build()
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }
}
