package com.sgs.ugh.security

import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.entity.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.test.context.support.WithSecurityContextFactory

class MockSecurityContextFactory: WithSecurityContextFactory<MockUser> {
    override fun createSecurityContext(annotation: MockUser): SecurityContext {
        val user = CustomerDto(annotation.id, annotation.username, annotation.email, annotation.password)

        val userDetails = CustomUserDetails(user)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = usernamePasswordAuthenticationToken
        return context
    }
}