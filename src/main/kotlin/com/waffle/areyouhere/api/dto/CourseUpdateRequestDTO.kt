package com.waffle.areyouhere.api.dto

import jakarta.validation.constraints.NotEmpty

data class CourseUpdateRequestDTO(
    @NotEmpty
    val name: String,
    @NotEmpty
    val description: String,
    @NotEmpty
    val onlyListNameAllowed: Boolean,
)
