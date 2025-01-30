package com.waffle.areyouhere.core.course.service

import com.waffle.areyouhere.core.course.dto.CourseDTO
import com.waffle.areyouhere.core.course.model.Course
import com.waffle.areyouhere.core.course.repository.CourseRepository
import com.waffle.areyouhere.core.manager.repository.ManagerRepository
import com.waffle.areyouhere.crossConcern.error.CourseNotFoundException
import com.waffle.areyouhere.crossConcern.error.UnauthenticatedException
import com.waffle.areyouhere.crossConcern.error.UnauthorizedCourseException
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val managerRepository: ManagerRepository,
) {
    suspend fun create(
        managerId: Long,
        name: String,
        description: String,
        allowOnlyRegistered: Boolean,
    ): CourseDTO {
        val manager = managerRepository.findById(managerId) ?: throw UnauthenticatedException
        courseRepository.save(
            Course(
                manager = manager,
                name = name,
                description = description,
                allowOnlyRegistered = allowOnlyRegistered,
            ),
        ).let { return CourseDTO(it) }
    }

    suspend fun update(
        managerId: Long,
        courseId: Long,
        name: String,
        description: String,
        allowOnlyRegistered: Boolean,
    ) {
        val manager = managerRepository.findById(managerId) ?: throw UnauthenticatedException
        val course = courseRepository.findById(courseId) ?: throw CourseNotFoundException
        if (course.manager != manager) {
            throw UnauthorizedCourseException
        }
        course.apply {
            this.manager = manager
            this.name = name
            this.description = description
            this.allowOnlyRegistered = allowOnlyRegistered
        }
        courseRepository.save(course)
    }

    suspend fun delete(managerId: Long, courseId: Long) {
        val manager = managerRepository.findById(managerId) ?: throw UnauthenticatedException
        val course = courseRepository.findById(courseId) ?: throw CourseNotFoundException
        if (course.manager != manager) {
            throw UnauthorizedCourseException
        }
        courseRepository.deleteById(courseId)
    }
}
