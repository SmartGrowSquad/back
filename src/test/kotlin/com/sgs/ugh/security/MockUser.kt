package com.sgs.ugh.security

import org.springframework.security.test.context.support.WithSecurityContext


@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory::class)
annotation class MockUser(
    val id: Long = 1L,
    val username: String = "username",
    val password: String = "encoded password",
    val email: String = "test@email.com",
    val role: String = "ROLE_USER"
)