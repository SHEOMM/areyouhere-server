package com.waffle.areyouhere.core.course.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import com.waffle.areyouhere.core.course.model.Course
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.util.BaseRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

@Repository
class CourseRepository(
    override val sessionFactory: SessionFactory,
    override val context: JpqlRenderContext,
) : BaseRepository<Course, Long>(sessionFactory, context, Course::class, Course::id) {
    suspend fun findAllByManager(managerId: Long): List<Course> {
        val query = jpql {
            select(entity(Course::class)).from(entity(Course::class))
                .where(path(Course::manager).path(Manager::id).eq(managerId))
        }
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).resultList
        }.awaitSuspending()
    }
}
