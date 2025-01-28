package com.waffle.areyouhere.config.database

import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.Persistence
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SessionFactoryConfiguration {

    @Bean
    fun getSessionFactory(): Mutiny.SessionFactory {
        return Persistence
            .createEntityManagerFactory("areyouhere-local")
            .unwrap(Mutiny.SessionFactory::class.java)
    }

    @Bean
    fun getJPQLRenderContext(): JpqlRenderContext {
        return jpqlRenderContext
    }

    @Bean
    fun getJPQLRenderer(): JpqlRenderer {
        return jpqlRenderer
    }

    private val jpqlRenderContext = JpqlRenderContext()

    private val jpqlRenderer = JpqlRenderer()
}
