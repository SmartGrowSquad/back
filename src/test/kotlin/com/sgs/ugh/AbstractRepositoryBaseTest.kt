package com.sgs.ugh

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * mariaDB Test
 */

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
abstract class AbstractRepositoryBaseTest: DescribeSpec() {
    companion object {
        private val mariaDBContainer = MariaDBContainer<Nothing>("mariadb")
            .apply {
                withDatabaseName("test_db")
                withUsername("test_user")
                withPassword("test_password")
                withExposedPorts(3306)
            }
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mariaDBContainer::getUsername)
            registry.add("spring.datasource.password", mariaDBContainer::getPassword)
        }
        init {
            mariaDBContainer.start()
        }
    }
}