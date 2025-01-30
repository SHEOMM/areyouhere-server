package com.waffle.areyouhere.api.dto

import com.waffle.areyouhere.core.course.dto.CourseDTO

data class AllCourseResponseDTO(
    val courses: List<CourseDTO>,
)
