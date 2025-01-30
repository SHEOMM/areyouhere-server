package com.waffle.areyouhere.api.dto

import jakarta.validation.constraints.NotEmpty

data class CourseCreationRequestDTO(
    @NotEmpty
    val name: String,
    @NotEmpty
    val description: String,
//    val attendees: List<AttendeeData>,
    @NotEmpty
    val onlyListNameAllowed: Boolean,
)
