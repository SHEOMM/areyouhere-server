package com.waffle.areyouhere.core.manager.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.core.util.CustomJpql
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ManagerRepository(
    private val sessionFactory: SessionFactory,
    private val context: JpqlRenderContext,
) {

    fun findById(id: Long): Mono<Manager> {
        val query = jpql(CustomJpql) {
            selectFrom(Manager::class)
                .where(path(Manager::id).eq(id))
        }
        return sessionFactory.withSession {
            it.createQuery(query, context).setMaxResults(1).singleResult
        }.convert().with(UniReactorConverters.toMono())
    }
}
