package com.waffle.areyouhere.core.section.domain.entity

import com.waffle.areyouhere.core.attendee.domain.entity.Attendee
import com.waffle.areyouhere.core.course.domain.entity.Course
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Section (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    var isDeactivated: Boolean, // TODO: 필요한지 논의.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    var course: Course,
)