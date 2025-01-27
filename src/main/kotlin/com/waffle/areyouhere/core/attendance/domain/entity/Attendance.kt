package com.waffle.areyouhere.core.attendance.domain.entity

import com.waffle.areyouhere.core.attendee.domain.entity.Attendee
import com.waffle.areyouhere.core.section.domain.entity.Section
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "attendance")
class Attendance (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 16, nullable = false)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    var attendee: Attendee,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    var section: Section,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: AttendanceType

)