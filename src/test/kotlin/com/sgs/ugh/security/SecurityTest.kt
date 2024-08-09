package com.sgs.ugh.security

import com.sgs.ugh.AbstractIntegrationAppTest
import io.kotest.matchers.shouldBe
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.web.servlet.MockMvc

@MockUser
class SecurityTest: AbstractIntegrationAppTest() {
    private val log = LoggerFactory.getLogger(SecurityTest::class.java)
    init {
        describe("security test") {
            context("get authenticated users from context") {
                val authentication = SecurityContextHolder.getContext().authentication
                val details: UserDetails = authentication.principal as UserDetails

                it("test") {
                    details.username shouldBe "test@email.com"
                }
            }
            describe("만료된 엑세스 토큰") {}
        }
    }
}