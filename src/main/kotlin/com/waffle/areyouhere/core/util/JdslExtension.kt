package com.waffle.areyouhere.core.util

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.JpqlDsl
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryFromStep
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import com.linecorp.kotlinjdsl.querymodel.QueryPart
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressionable
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressions
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicate
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicates
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlRenderSerializer
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlSerializer
import com.linecorp.kotlinjdsl.render.jpql.writer.JpqlWriter
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

data class JpqlLimit<T : Any>(
    val selectQuery: QueryPart,
    val limit: Long,
    override val returnType: KClass<T>,
) : SelectQuery<T>

object JpqlLimitSerializer : JpqlSerializer<JpqlLimit<*>> {
    override fun handledType(): KClass<JpqlLimit<*>> {
        return JpqlLimit::class
    }

    override fun serialize(
        part: JpqlLimit<*>,
        writer: JpqlWriter,
        context: RenderContext,
    ) {
        val delegate = context.getValue(JpqlRenderSerializer)

        delegate.serialize(part.selectQuery, writer, context)

        writer.write(" LIMIT ${part.limit}")
    }
}

class CustomJpql : Jpql() {
    companion object Constructor : JpqlDsl.Constructor<CustomJpql> {
        override fun newInstance(): CustomJpql = CustomJpql()
    }

    // in 절이 비어있을 때 에러 대신 match가 안되도록 한다.
    private val falsePredicate get() = booleanLiteral(false).eq(booleanLiteral(true))

    fun <T : Any, S : T?> Expressionable<T>.`in`(compareValues: Collection<S>): Predicate {
        if (compareValues.isEmpty()) {
            return falsePredicate
        }

        return Predicates.`in`(this.toExpression(), compareValues.map { Expressions.value(it) })
    }
    inline fun <reified T : Any> JpqlQueryable<SelectQuery<T>>.limit(limit: Int): JpqlLimit<T> {
        return JpqlLimit(
            this.toQuery(),
            limit.toLong(),
            T::class,
        )
    }

    // select(entity).from(entity)의 반복을 막기 위해 from 절을 그대로 반환하는 custom dsl

    inline fun <reified T : Any> selectFrom(type: KClass<T>): SelectQueryWhereStep<T> {
        val entity = entity(type)
        return select(entity).from(entity)
    }
}


