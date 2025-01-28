package com.waffle.areyouhere.core.manager.repository

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ManagerRepositoryTest {

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    @Test
    fun `레포지토리 첫 테스트`() {
        runBlocking {
            println(managerRepository.findById(123L))
        }
    }
}
