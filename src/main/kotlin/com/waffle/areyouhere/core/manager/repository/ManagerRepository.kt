package com.waffle.areyouhere.core.manager.repository

import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.util.BaseRepository
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

@Repository
class ManagerRepository(
    override val sessionFactory: SessionFactory,
    override val context: JpqlRenderContext,
) : BaseRepository<Manager, Long>(sessionFactory, context, Manager::class, Manager::id)
