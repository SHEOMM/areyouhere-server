package com.waffle.areyouhere.core.manager.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.util.BaseRepository
import com.waffle.areyouhere.util.CustomJpql
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

// FIXME: 구현 상 세션이 매번 새로 열린다.
@Repository
class ManagerRepository(
    override val sessionFactory: SessionFactory,
    override val context: JpqlRenderContext,
) : BaseRepository<Manager, Long>(sessionFactory, context, Manager::class, Manager::id) {
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
}
