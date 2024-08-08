package com.sgs.ugh

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
abstract class AbstractIntegrationAppTest {
    companion object {
        private val redisContainer = GenericContainer<Nothing>("redis:6-alpine")
            .apply { withExposedPorts(6379) }

        private val mariaDBContainer = MariaDBContainer<Nothing>("mariadb")
            .apply {
                withDatabaseName("test_db")
                withUsername("test_user")
                withPassword("test_password")
                withExposedPorts(3306)
            }

        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redisContainer::getHost)
            registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort)
            registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mariaDBContainer::getUsername)
            registry.add("spring.datasource.password", mariaDBContainer::getPassword)

        }
    }
}