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
            val manager2 = Manager(
                email = "aa@naver.com",
                name = "ikjun",
                password = "hehe",
            )

            val savedManager = managerRepository.save(manager)
            val savedManager2 = managerRepository.save(manager2)
            val foundManager = managerRepository.findById(manager.id!!)
            assertEquals(savedManager.id, foundManager!!.id)
            val foundManager2 = managerRepository.findById(manager2.id!!)
            assertEquals(savedManager2.id, foundManager2!!.id)

            val allManagers = managerRepository.findAll()
            assertEquals(2, allManagers.size)
            val managerCount = managerRepository.count()
            assertEquals(2, managerCount)

            val deleteCount = managerRepository.deleteById(manager.id!!)
            assertEquals(1, deleteCount)

            val managerCount2 = managerRepository.count()
            assertEquals(1, managerCount2)

            val notFoundManager = managerRepository.findById(manager.id!!)
            assertEquals(null, notFoundManager)

            val hasManager = managerRepository.existsById(manager.id!!)
            assertEquals(false, hasManager)
            val hasManager2 = managerRepository.existsById(manager2.id!!)
            assertEquals(true, hasManager2)
        }
    }
}
