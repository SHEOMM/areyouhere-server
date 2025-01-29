package com.waffle.areyouhere.util

import com.linecorp.kotlinjdsl.querymodel.jpql.delete.DeleteQueries
import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entities
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressions
import com.linecorp.kotlinjdsl.querymodel.jpql.path.Paths
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicates
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQueries
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createMutationQuery
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Base repository class for CRUD operations on entities with id.
 *
 * @param T The entity type.
 * @param ID The id type of the entity.
 * @param sessionFactory The Hibernate SessionFactory for database operations.
 * @param context The JpqlRenderContext for rendering JPQL queries.
 * @param idRef A reference to the id property of the entity.
 */
open class BaseRepository<T : Any, ID : Any>(
    open val sessionFactory: SessionFactory,
    open val context: JpqlRenderContext,
    open val entityClass: KClass<T>,
    open val idRef: KProperty1<T, ID?>,
) {

    suspend fun save(entity: T): T {
        return sessionFactory.withTransaction { session ->
            session.persist(entity)
                .chain(session::flush)
                .replaceWith(entity)
        }.awaitSuspending()
    }

    suspend fun findById(id: ID): T? {
        val query = SelectQueries.selectQuery(
            returnType = entityClass,
            distinct = false,
            select = listOf(Entities.entity(entityClass)),
            from = listOf(Entities.entity(entityClass)),
            where = Predicates.equal(
                Paths.path(Entities.entity(entityClass), idRef).toExpression(),
                Expressions.value(id),
            ),
        )
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).setMaxResults(1).singleResultOrNull
        }.awaitSuspending()
    }

    suspend fun deleteById(id: ID): Int? {
        val query = DeleteQueries.deleteQuery(
            entity = Entities.entity(entityClass),
            where = Predicates.equal(
                Paths.path(Entities.entity(entityClass), idRef).toExpression(),
                Expressions.value(id),
            ),
        )
        return sessionFactory.withTransaction { session, tx ->
            session.createMutationQuery(query, context).executeUpdate()
        }.awaitSuspending()
    }

    suspend fun findAll(): List<T> {
        val query = SelectQueries.selectQuery(
            returnType = entityClass,
            distinct = false,
            select = listOf(Entities.entity(entityClass)),
            from = listOf(Entities.entity(entityClass)),
        )
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).resultList
        }.awaitSuspending()
    }

    suspend fun existsById(id: ID): Boolean {
        val query = SelectQueries.selectQuery(
            returnType = Long::class,
            distinct = false,
            select = listOf(Expressions.count(distinct = false, Paths.path(idRef).toExpression())),
            from = listOf(Entities.entity(entityClass)),
            where = Predicates.equal(
                Paths.path(Entities.entity(entityClass), idRef).toExpression(),
                Expressions.value(id),
            ),
        )
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).setMaxResults(1).singleResult
        }.awaitSuspending() > 0
    }

    suspend fun count(): Long {
        val query = SelectQueries.selectQuery(
            returnType = Long::class,
            distinct = false,
            select = listOf(Expressions.count(distinct = false, Paths.path(idRef).toExpression())),
            from = listOf(Entities.entity(entityClass)),
        )
        return sessionFactory.withTransaction { session ->
            session.createQuery(query, context).setMaxResults(1).singleResult
        }.awaitSuspending() ?: 0
    }
}
