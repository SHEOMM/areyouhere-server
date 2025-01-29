package com.waffle.areyouhere.core.manager.repository

import com.waffle.areyouhere.core.manager.model.Manager
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class ManagerRepositoryTest {

    @Autowired
    private lateinit var managerRepository: ManagerRepository

    @Test
    fun `레포지토리 첫 테스트`() {
        runBlocking {
            val manager = Manager(
                email = "hellp@naver.com",
                name = "seongho",
                password = "hi",
            )
            val saveManager = managerRepository.save(manager)
            val foundManager = managerRepository.findById(manager.id!!)
            assertEquals(saveManager.id, foundManager!!.id)

            val deleteCount = managerRepository.deleteById(manager.id!!)
            assertEquals(1, deleteCount)

            val notFoundManager = managerRepository.findById(manager.id!!)
            assertEquals(null, notFoundManager)
        }
    }
}
