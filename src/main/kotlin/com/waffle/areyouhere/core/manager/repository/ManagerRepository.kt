package com.waffle.areyouhere.core.manager.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createMutationQuery
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.util.CustomJpql
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import kotlinx.coroutines.reactor.awaitSingle
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
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).setMaxResults(1).singleResult
        }.convert().with(UniReactorConverters.toMono())
            .awaitSingle()
    }

    suspend fun save(manager: Manager): Manager {
        return sessionFactory.withTransaction { session ->
            session.persist(manager).chain(session::flush).replaceWith(manager)
        }.convert().with(UniReactorConverters.toMono())
            .awaitSingle()
    }

    suspend fun deleteById(id: Long): Int? {
        val query = jpql(CustomJpql) {
            deleteFrom(entity(Manager::class))
                .where(path(Manager::id).eq(id))
        }
        return sessionFactory.withTransaction { session, tx ->
            session.createMutationQuery(query, context).executeUpdate()
        }.convert().with(UniReactorConverters.toMono())
            .awaitSingle()
    }
}
