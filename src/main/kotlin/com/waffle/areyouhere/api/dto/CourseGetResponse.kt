package com.waffle.areyouhere.api.dto

data class CourseGetResponse(
    val id: Long,
    val name: String,
    val description: String,
    val allowOnlyRegistered: Boolean,
)
