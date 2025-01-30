package com.waffle.areyouhere.core.course.dto

import com.waffle.areyouhere.core.course.model.Course

data class CourseDTO(
    val id: Long,
    val name: String,
    val description: String,
    val allowOnlyRegistered: Boolean,
    val attendees: Long,
) {
    /**
     * Constructor for creating a CourseDTO from a Course object. Note that the attendees field is not set.
     */
    constructor(course: Course) : this(
        id = course.id!!,
        name = course.name,
        description = course.description ?: "",
        allowOnlyRegistered = course.allowOnlyRegistered,
        attendees = 0,
    )
}
