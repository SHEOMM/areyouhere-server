package com.waffle.areyouhere.core.manager.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createMutationQuery
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.util.CustomJpql
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

@Repository
class ManagerRepository(
    private val sessionFactory: SessionFactory,
    private val context: JpqlRenderContext,
) {

    suspend fun findById(id: Long): Manager {
        val query = jpql(CustomJpql) {
            selectFrom(Manager::class)
                .where(path(Manager::id).eq(id))
        }
        return sessionFactory.withSession {
            it.createQuery(query, context).setMaxResults(1).singleResult
        }.awaitSuspending()
    }

    // TODO: exists extension function 만들어서 효율좋게 해보기.
    suspend fun findByEmail(email: String): Manager? {
        val query = jpql(CustomJpql) {
            selectFrom(Manager::class)
                .where(path(Manager::email).eq(email))
        }
        return sessionFactory.withSession {
            it.createQuery(query, context).setMaxResults(1).resultList
        }.awaitSuspending().firstOrNull()
    }

    suspend fun save(manager: Manager): Manager {
        return sessionFactory.withTransaction { session, tx ->
            session.persist(manager).chain(session::flush).replaceWith(manager)
        }.awaitSuspending()
    }

    suspend fun deleteById(id: Long) {
        val query = jpql(CustomJpql) {
            deleteFrom(entity(Manager::class))
                .where(path(Manager::id).eq(id))
        }
        sessionFactory.withTransaction { session, tx ->
            session.createMutationQuery(query, context).executeUpdate()
        }.awaitSuspending()
    }
}
