package com.waffle.areyouhere.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class LoginRequestDTO(
    @NotEmpty
    @Email(
        message = "유효하지 않은 이메일 형식입니다.",
        regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
    )
    val email: String,
    @NotEmpty
    val password: String,
)
